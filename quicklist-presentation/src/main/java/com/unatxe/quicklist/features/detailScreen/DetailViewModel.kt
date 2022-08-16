package com.unatxe.quicklist.features.detailScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unatxe.quicklist.domain.interactors.GetListUseCase
import com.unatxe.quicklist.domain.interactors.UpdateListItemUseCase
import com.unatxe.quicklist.domain.interactors.UpdateListUseCase
import com.unatxe.quicklist.entities.QListCompose
import com.unatxe.quicklist.entities.QListItemType
import com.unatxe.quicklist.entities.QListItemType.Companion.update
import com.unatxe.quicklist.entities.sortPositions
import com.unatxe.quicklist.helpers.evenArray
import com.unatxe.quicklist.navigation.NavigationDirections
import com.unatxe.quicklist.navigation.NavigationDirections.ListScreen.NO_VALUE
import com.unatxe.quicklist.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
@Stable
class ListViewModel @Inject constructor(
    private val getListUseCase: GetListUseCase,
    private val updateListItemUseCase: UpdateListItemUseCase,
    private val updateListUseCase: UpdateListUseCase,
    private val navigationManager: NavigationManager,
    savedStateHandle: SavedStateHandle
) : ViewModel(), IListViewModel {

    private val idList: Int

    override var uiState = mutableStateOf(QListCompose())
        private set

    override var numCheckedItems: MutableState<Int> = mutableStateOf(0)

    private val itemListInitialState: MutableList<QListItemType> = mutableListOf()

    override val isCreationMode: Boolean
        get() = idList == NO_VALUE

    override var showUncheckedItems: MutableState<Boolean> = mutableStateOf(true)

    init {
        idList = savedStateHandle.get<Int>(NavigationDirections.ListScreen.KEY_LIST_ID)
            ?: NO_VALUE
    }

    override val initData: Unit by lazy {

        if (idList != NO_VALUE) {
            viewModelScope.launch(Dispatchers.IO) {
                getListUseCase.invoke(idList).collect {
                    Log.d("Test", "List updated")
                    val mapped = QListCompose.from(it)
                    withContext(Dispatchers.Main) {
                        uiState.value = mapped
                    }

                    itemListInitialState.evenArray(mapped.items)
                    itemListInitialState.update(mapped.items)

                    uiState.value?.items?.let { qListItemType ->
                        qListItemType.evenArray(mapped.items)
                        qListItemType.sortPositions()
                        uiState.value!!.update(mapped)
                    }
                    showItemsDone()
                    hideItemsDone()
                }
            }
        }
        Unit
    }

    override fun doneClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            showUncheckedItems.value = showUncheckedItems.value.not()
            hideItemsDone()
        }
    }

    private fun hideItemsDone() {
        if (showUncheckedItems.value) {
            uiState.value?.items?.evenArray(itemListInitialState)
        } else {
            val modifiedArray = itemListInitialState.filter {
                (it is QListItemType.QListItemCheckBox && it.checked.value).not()
            }
            uiState.value?.items?.evenArray(modifiedArray)
        }
    }

    private fun showItemsDone() {
        numCheckedItems.value = uiState.value?.items
            ?.count { it is QListItemType.QListItemCheckBox && it.checked.value } ?: 0
    }

    override fun onCheckBoxChange(qLisItem: QListItemType.QListItemCheckBox) {
        viewModelScope.launch(Dispatchers.IO) {
            qLisItem.checked.value = qLisItem.checked.value.not()
            val qListItem = QListItemType.to(qLisItem)
            updateListItemUseCase.invoke(qListItem).collect {}
        }
    }

    private val testTask: MutableMap<QListItemType, Timer> = mutableMapOf()

    override fun onListItemValueChange(
        qListItem: QListItemType.QListItemCheckBox,
        valueItem: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (testTask.containsKey(qListItem)) {
                val timer = testTask.getValue(qListItem)
                timer.cancel()
            }
            val timer = Timer()
            testTask[qListItem] = timer

            timer.schedule(
                object : TimerTask() {
                    override fun run() {
                        Log.d("Test", "Update Item Text")
                        testTask.remove(qListItem)
                        qListItem.text.value = valueItem
                        val qListItemUpdated = QListItemType.to(qListItem)
                        viewModelScope.launch(Dispatchers.IO) {
                            updateListItemUseCase.invoke(qListItemUpdated).collect()
                        }
                    }
                },
                500
            )
        }
    }

    override fun onBackClicked() {
        navigationManager.navController?.popBackStack()
    }

    override fun onFavoriteClick(qListCompose: QListCompose) {
        uiState.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                it.isFavourite.value = it.isFavourite.value.not()
                updateListUseCase.invoke(QListCompose.to(it)).collect {}
            }
        }
    }

    override fun addItem() {
    }

    override fun onEventReceived(detailViewModelEvent: DetailViewModelEvent) {
        when (detailViewModelEvent) {
            is DetailViewModelEvent.FocusRequest -> {
                processFocusRequest(detailViewModelEvent.qListItemCheckBox)
            }
            is DetailViewModelEvent.EditRequest -> {
                detailViewModelEvent.qListItemCheckBox?.let {
                    processFocusRequest(it, true)
                }
                processEditRequest()
            }
            is DetailViewModelEvent.ListItemValueChange -> onListItemValueChange(
                detailViewModelEvent.itemCheckBox,
                detailViewModelEvent.textChanged
            )
        }
    }

    private fun processFocusRequest(
        qListItemCheckBox: QListItemType.QListItemCheckBox,
        force: Boolean = false
    ) {
        if (force) {
            qListItemCheckBox.isFocused.value = true
        } else {
            qListItemCheckBox.isFocused.value = qListItemCheckBox.isFocused.value.not()
        }

        uiState.value.isEditMode.value = false
        qListItemCheckBox.isEditMode.value = false
        uiState.value.itemIsSelected.value = qListItemCheckBox.isFocused.value

        itemListInitialState.forEach {
            if (it is QListItemType.QListItemCheckBox && it.id != qListItemCheckBox.id && it.isFocused.value) {
                it.isFocused.value = false
                it.isEditMode.value = false
            }
        }

        if (qListItemCheckBox.isFocused.value) {
            uiState.value.componentSelected.value = qListItemCheckBox
        } else {
            uiState.value.componentSelected.value = null
        }
    }

    private fun processEditRequest() {
        val itemSelected = itemListInitialState.find {
            if (it is QListItemType.QListItemCheckBox) {
                it.isFocused.value
            } else {
                false
            }
        } as QListItemType.QListItemCheckBox?
        itemSelected?.let {
            it.isEditMode.value = it.isEditMode.value.not()
            uiState.value.isEditMode.value = it.isEditMode.value
        }
    }
}

interface IListViewModel {
    val uiState: MutableState<QListCompose>
    val isCreationMode: Boolean
    var showUncheckedItems: MutableState<Boolean>
    var numCheckedItems: MutableState<Int>

    val initData: Unit
    fun doneClicked()
    fun onCheckBoxChange(qLisItem: QListItemType.QListItemCheckBox)
    fun onBackClicked()
    fun onFavoriteClick(qListCompose: QListCompose)

    fun onListItemValueChange(qListItem: QListItemType.QListItemCheckBox, valueItem: String)
    fun addItem()

    fun onEventReceived(detailViewModelEvent: DetailViewModelEvent)
}

sealed class DetailViewModelEvent {
    class FocusRequest(val qListItemCheckBox: QListItemType.QListItemCheckBox) : DetailViewModelEvent()
    class EditRequest(val qListItemCheckBox: QListItemType.QListItemCheckBox? = null) : DetailViewModelEvent()
    class ListItemValueChange(
        val itemCheckBox: QListItemType.QListItemCheckBox,
        val textChanged: String
    ) : DetailViewModelEvent()
}
