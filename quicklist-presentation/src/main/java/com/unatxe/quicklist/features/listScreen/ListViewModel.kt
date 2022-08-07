package com.unatxe.quicklist.features.listScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unatxe.quicklist.domain.interactors.GetListUseCase
import com.unatxe.quicklist.entities.QListCompose
import com.unatxe.quicklist.navigation.NavigationDirections
import com.unatxe.quicklist.navigation.NavigationDirections.ListScreen.NO_VALUE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getListUseCase: GetListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), IListViewModel {

    private val idList: Int

    override val uiState: MutableState<QListCompose?>
    override val isCreationMode: Boolean
        get() = idList == NO_VALUE

    init {
        idList = savedStateHandle.get<Int>(NavigationDirections.ListScreen.KEY_LIST_ID)
            ?: NO_VALUE

        uiState = mutableStateOf(null)

        if (idList != NO_VALUE) {
            viewModelScope.launch {
                getListUseCase.invoke(idList).collect {
                    uiState.value = QListCompose.from(it)
                }
            }
        }
    }
}

interface IListViewModel {

    val uiState: MutableState<QListCompose?>

    val isCreationMode: Boolean
}
