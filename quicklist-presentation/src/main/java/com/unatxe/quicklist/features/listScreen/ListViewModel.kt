package com.unatxe.quicklist.features.listScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unatxe.quicklist.domain.interactors.GetListUseCase
import com.unatxe.quicklist.entities.QListCompose
import com.unatxe.quicklist.entities.QListItemType
import com.unatxe.quicklist.entities.sortPositions
import com.unatxe.quicklist.helpers.evenArray
import com.unatxe.quicklist.navigation.NavigationDirections
import com.unatxe.quicklist.navigation.NavigationDirections.ListScreen.NO_VALUE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
@Stable
class ListViewModel @Inject constructor(
    private val getListUseCase: GetListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), IListViewModel {

    private val idList: Int

    override var uiState = mutableStateListOf<QListItemType>()
        private set

    override var numCheckedItems: MutableState<Int> = mutableStateOf(0)

    private val initialUIState: MutableList<QListItemType> = mutableListOf()

    override val isCreationMode: Boolean
        get() = idList == NO_VALUE

    override var showUncheckedItems: MutableState<Boolean> = mutableStateOf(true)

    init {
        idList = savedStateHandle.get<Int>(NavigationDirections.ListScreen.KEY_LIST_ID)
            ?: NO_VALUE

        if (idList != NO_VALUE) {
            viewModelScope.launch {
                getListUseCase.invoke(idList).collect {
                    val mapped = QListCompose.from(it)
                    initialUIState.evenArray(mapped.items)
                    uiState.evenArray(mapped.items)
                    showItemsDone()
                }
            }
        }
    }

    override fun doneClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            showUncheckedItems.value = showUncheckedItems.value.not()
            hideItemsDone()
        }
    }

    private fun hideItemsDone(){
        if (showUncheckedItems.value) {
            uiState.evenArray(initialUIState)
        } else {
            val modifiedArray = initialUIState.filter {
                (it is QListItemType.QListItemCheckBox && it.checked.value).not()
            }
            uiState.evenArray(modifiedArray)
        }
    }

    private fun showItemsDone() {
        numCheckedItems.value = initialUIState
            .count { it is QListItemType.QListItemCheckBox && it.checked.value }
    }

    override fun onCheckBoxChange(qLisItem: QListItemType.QListItemCheckBox) {
        viewModelScope.launch(Dispatchers.IO) {
            qLisItem.checked.value = qLisItem.checked.value.not()
            uiState.sortPositions()
            showItemsDone();
            hideItemsDone()
        }

    }
}
@Stable
interface IListViewModel {
    val uiState: SnapshotStateList<QListItemType>
    val isCreationMode: Boolean
    var showUncheckedItems: MutableState<Boolean>
    var numCheckedItems: MutableState<Int>

    fun doneClicked()
    fun onCheckBoxChange(qLisItem: QListItemType.QListItemCheckBox)
}
