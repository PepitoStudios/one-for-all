package com.unatxe.quicklist.features.listScreen

import androidx.compose.runtime.mutableStateListOf
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
class ListViewModel @Inject constructor(
    private val getListUseCase: GetListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), IListViewModel {

    private val idList: Int
    var doneClicked = false
    override fun doneClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            if (doneClicked) {
                uiState.evenArray(initialUIState)
            } else {
                val modifiedArray = initialUIState.filter {
                    (it is QListItemType.QListItemCheckBox && it.checked.value).not()
                }

                uiState.evenArray(modifiedArray)
            }
            doneClicked = doneClicked.not()
        }
    }

    override fun onCheckBoxChange(qLisItem: QListItemType.QListItemCheckBox) {
        qLisItem.checked.value = qLisItem.checked.value.not()
        uiState.sortPositions()
    }

    override var uiState = mutableStateListOf<QListItemType>()
        private set

    private val initialUIState: MutableList<QListItemType> = mutableListOf()

    override val isCreationMode: Boolean
        get() = idList == NO_VALUE

    init {
        idList = savedStateHandle.get<Int>(NavigationDirections.ListScreen.KEY_LIST_ID)
            ?: NO_VALUE

        if (idList != NO_VALUE) {
            viewModelScope.launch {
                getListUseCase.invoke(idList).collect {
                    val mapped = QListCompose.from(it)
                    initialUIState.evenArray(mapped.items)
                    uiState.evenArray(mapped.items)
                }
            }
        }
    }
}

interface IListViewModel {
    fun doneClicked()
    fun onCheckBoxChange(qLisItem: QListItemType.QListItemCheckBox)

    val uiState: SnapshotStateList<QListItemType>

    val isCreationMode: Boolean
}
