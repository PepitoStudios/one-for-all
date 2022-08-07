package com.unatxe.quicklist.features.mainScreen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unatxe.quicklist.domain.interactors.GetListsUseCase
import com.unatxe.quicklist.domain.interactors.UpdateListUseCase
import com.unatxe.quicklist.entities.QListCompose
import com.unatxe.quicklist.entities.QListCompose.Companion.update
import com.unatxe.quicklist.helpers.even
import com.unatxe.quicklist.navigation.NavigationDirections
import com.unatxe.quicklist.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getListsUseCase: GetListsUseCase,
    private val updateListUseCase: UpdateListUseCase,
    private val navigationManager: NavigationManager
) : ViewModel(), IMainViewModel {
    override fun listClicked(it: Int?) {
        Log.d("MainViewModel", "List clicked with id: $it")
        navigationManager.navigate(NavigationDirections.ListScreen.listScreen(it))
    }

    private val initialQList: MutableList<QListCompose> = mutableListOf()

    override var uiState = mutableStateListOf<QListCompose>()
        private set

    var listToSearch = ""

    override fun searchChanged(listToSearch: String) {
        this.listToSearch = listToSearch
        viewModelScope.launch(Dispatchers.IO) {
            val finalList = initialQList.filter {
                it.name.lowercase().contains(listToSearch.lowercase())
            }
            uiState.even(finalList)

            uiState.sortBy {
                it.id
            }
        }
    }

    override fun favouriteClicked(listToUpdate: QListCompose) {
        viewModelScope.launch(Dispatchers.IO) {
            listToUpdate.isFavourite.value = listToUpdate.isFavourite.value.not()
            updateListUseCase.invoke(QListCompose.to(listToUpdate)).collect {
                // val favouritePosition = uiState.indexOf(listToUpdate)
                // uiState[favouritePosition].update(QListCompose.from(it))
            }
        }
    }

    override val updateList: Unit by lazy {
        viewModelScope.launch((Dispatchers.IO)) {
            getListsUseCase().collect {
                val updateListCompose = QListCompose.from(it)
                initialQList.even(updateListCompose)
                initialQList.update(updateListCompose)
                searchChanged(listToSearch)
            }
        }
        Unit
    }
}

interface IMainViewModel {
    fun listClicked(it: Int?)
    fun searchChanged(listToSearch: String)
    fun favouriteClicked(it: QListCompose)

    val updateList: Unit

    val uiState: SnapshotStateList<QListCompose>
}
