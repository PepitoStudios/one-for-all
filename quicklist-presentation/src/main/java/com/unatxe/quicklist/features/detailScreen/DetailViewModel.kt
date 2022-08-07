package com.unatxe.quicklist.features.detailScreen

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
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    override var uiState = mutableStateOf<QListCompose?>(null)
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
                    val mapped = QListCompose.from(it)
                    if (uiState.value == null) {
                        uiState.value = mapped
                    }
                    itemListInitialState.evenArray(mapped.items)
                    itemListInitialState.update(mapped.items)

                    uiState.value?.items?.evenArray(mapped.items)
                    uiState.value?.items?.sortPositions()
                    uiState.value!!.update(mapped)
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
        numCheckedItems.value = itemListInitialState
            .count { it is QListItemType.QListItemCheckBox && it.checked.value }
    }

    override fun onCheckBoxChange(qLisItem: QListItemType.QListItemCheckBox) {
        viewModelScope.launch(Dispatchers.IO) {
            qLisItem.checked.value = qLisItem.checked.value.not()
            val qListItem = QListItemType.to(qLisItem)
            updateListItemUseCase.invoke(qListItem).collect {}
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
}

interface IListViewModel {
    val uiState: MutableState<QListCompose?>
    val isCreationMode: Boolean
    var showUncheckedItems: MutableState<Boolean>
    var numCheckedItems: MutableState<Int>

    val initData: Unit
    fun doneClicked()
    fun onCheckBoxChange(qLisItem: QListItemType.QListItemCheckBox)
    fun onBackClicked()
    fun onFavoriteClick(qListCompose: QListCompose)
}
