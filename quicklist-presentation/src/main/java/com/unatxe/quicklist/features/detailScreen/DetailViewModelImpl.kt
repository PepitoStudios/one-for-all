package com.unatxe.quicklist.features.detailScreen

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unatxe.quicklist.domain.interactors.GetListUseCase
import com.unatxe.quicklist.domain.interactors.UpdateListItemUseCase
import com.unatxe.quicklist.domain.interactors.UpdateListUseCase
import com.unatxe.quicklist.entities.QList.item.QListItemViewCheckBox
import com.unatxe.quicklist.entities.qList.QListView
import com.unatxe.quicklist.entities.qList.QListViewImpl
import com.unatxe.quicklist.entities.qList.item.QListItemView
import com.unatxe.quicklist.entities.qList.item.QListItemView.Companion.update
import com.unatxe.quicklist.entities.qList.item.QListItemViewCheckBoxImpl
import com.unatxe.quicklist.entities.qList.item.sortPositions
import com.unatxe.quicklist.helpers.evenArray
import com.unatxe.quicklist.navigation.NavigationDirections
import com.unatxe.quicklist.navigation.NavigationDirections.ListScreen.NO_VALUE
import com.unatxe.quicklist.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : ViewModel(), DetailViewModel {

    private val idList: Int

    private val _viewState: MutableStateFlow<QListViewImpl> = MutableStateFlow(QListViewImpl())

    override val viewState: StateFlow<QListView> = _viewState.asStateFlow()

    private val itemListInitialState: MutableList<QListItemView> = mutableListOf()

    init {
        idList = savedStateHandle.get<Int>(NavigationDirections.ListScreen.KEY_LIST_ID)
            ?: NO_VALUE

        _viewState.value.isCreationMode.value = idList == NO_VALUE

        if (idList != NO_VALUE) {
            viewModelScope.launch(Dispatchers.IO) {
                getListUseCase.invoke(idList).collect {
                    Log.d("Test", "List updated")
                    val mapped = QListViewImpl.from(it)
                    withContext(Dispatchers.Main) {
                        _viewState.value = mapped
                    }

                    itemListInitialState.evenArray(mapped.items)
                    itemListInitialState.update(mapped.items)

                    _viewState.value.items.let { qListItemType ->
                        qListItemType.evenArray(mapped.items)
                        qListItemType.sortPositions()
                        _viewState.value.update(mapped)
                    }
                    showItemsDone()
                    hideItemsDone()
                }
            }
        }
    }

    override fun onEvent(detailViewModelEvent: DetailViewModelEvent) {
        when (detailViewModelEvent) {
            is DetailViewModelEvent.FocusRequest -> {
                processFocusRequest(detailViewModelEvent.qListItemCheckBox)
            }
            is DetailViewModelEvent.EditRequest -> {
                detailViewModelEvent.qListItemCheckBox?.let {
                    processFocusRequest(it as QListItemViewCheckBoxImpl, true)
                }
                processEditRequest()
            }
            is DetailViewModelEvent.ListItemValueChange -> onListItemValueChange(
                detailViewModelEvent.itemCheckBox as QListItemViewCheckBoxImpl,
                detailViewModelEvent.textChanged
            )
            DetailViewModelEvent.BackClicked -> backClicked()
            is DetailViewModelEvent.CheckBoxChange -> checkBoxChange(detailViewModelEvent.qListItem)
            DetailViewModelEvent.DoneClicked -> doneClicked()
            is DetailViewModelEvent.FavouriteClicked -> favoriteClick()
        }
    }
    private fun doneClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.value.showUncheckedItems.value =
                _viewState.value.showUncheckedItems.value.not()
            hideItemsDone()
        }
    }

    private fun hideItemsDone() {
        if (_viewState.value.showUncheckedItems.value) {
            _viewState.value.items.evenArray(itemListInitialState)
        } else {
            val modifiedArray = itemListInitialState.filter {
                (it is QListItemViewCheckBoxImpl && it.checked.value).not()
            }
            _viewState.value.items.evenArray(modifiedArray)
        }
    }

    private fun showItemsDone() {
        _viewState.value.numCheckedItems.value = _viewState.value.items
            .count { it is QListItemViewCheckBoxImpl && it.checked.value } ?: 0
    }

    private fun checkBoxChange(qLisItem: QListItemViewCheckBox) {
        viewModelScope.launch(Dispatchers.IO) {
            qLisItem as QListItemViewCheckBoxImpl
            qLisItem.checked.value = qLisItem.checked.value.not()
            val qListItem = QListItemView.to(qLisItem)
            updateListItemUseCase.invoke(qListItem).collect {}
        }
    }

    private val itemValueChangeMap: MutableMap<QListItemView, Timer> = mutableMapOf()

    private fun onListItemValueChange(
        qListItem: QListItemViewCheckBoxImpl,
        valueItem: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (itemValueChangeMap.containsKey(qListItem)) {
                val timer = itemValueChangeMap.getValue(qListItem)
                timer.cancel()
            }
            val timer = Timer()
            itemValueChangeMap[qListItem] = timer

            timer.schedule(
                object : TimerTask() {
                    override fun run() {
                        itemValueChangeMap.remove(qListItem)
                        qListItem.text.value = valueItem
                        val qListItemUpdated = QListItemView.to(qListItem)
                        viewModelScope.launch(Dispatchers.IO) {
                            updateListItemUseCase.invoke(qListItemUpdated).collect()
                        }
                    }
                },
                100
            )
        }
    }

    private fun backClicked() {
        navigationManager.navController?.popBackStack()
    }

    private fun favoriteClick() {
        _viewState.value.let {
            viewModelScope.launch(Dispatchers.IO) {
                it.isFavourite.value = it.isFavourite.value.not()
                updateListUseCase.invoke(QListViewImpl.to(it)).collect {}
            }
        }
    }

    private fun processFocusRequest(
        qListItemCheckBox: QListItemViewCheckBox,
        force: Boolean = false
    ) {
        qListItemCheckBox as QListItemViewCheckBoxImpl
        if (force) {
            qListItemCheckBox.isFocused.value = true
        } else {
            qListItemCheckBox.isFocused.value = qListItemCheckBox.isFocused.value.not()
        }

        _viewState.value.isEditMode.value = false
        qListItemCheckBox.isEditMode.value = false
        _viewState.value.itemIsSelected.value = qListItemCheckBox.isFocused.value

        itemListInitialState.forEach {
            if (it is QListItemViewCheckBoxImpl
            ) {
                if (it.id != qListItemCheckBox.id) {
                    it.isFocused.value = false
                    it.isEditMode.value = false
                }
                it.isCheckBoxEnabled.value = true
            }
        }

        if (qListItemCheckBox.isFocused.value) {
            _viewState.value.componentSelected.value = qListItemCheckBox
        } else {
            _viewState.value.componentSelected.value = null
        }
    }

    private fun processEditRequest() {
        val itemSelected = itemListInitialState.find {
            if (it is QListItemViewCheckBoxImpl) {
                it.isFocused.value
            } else {
                false
            }
        } as QListItemViewCheckBoxImpl?

        itemSelected?.let { itemSelected ->
            val isEditMode = itemSelected.isEditMode.value.not()
            itemSelected.isEditMode.value = isEditMode
            _viewState.value.isEditMode.value = isEditMode

            itemListInitialState.forEach { qListItem ->
                if (qListItem is QListItemViewCheckBoxImpl) {
                    qListItem.isCheckBoxEnabled.value = isEditMode.not()
                }
            }
        }
    }
}
