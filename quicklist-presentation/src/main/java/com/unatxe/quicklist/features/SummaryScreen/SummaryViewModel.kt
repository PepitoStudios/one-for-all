package com.unatxe.quicklist.features.SummaryScreen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unatxe.quicklist.domain.interactors.GetListsUseCase
import com.unatxe.quicklist.domain.interactors.UpdateListUseCase
import com.unatxe.quicklist.entities.qList.QListView
import com.unatxe.quicklist.entities.qList.QListViewImpl
import com.unatxe.quicklist.entities.qList.QListViewImpl.Companion.update
import com.unatxe.quicklist.helpers.evenArray
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

    private val initialQList: MutableList<QListViewImpl> = mutableListOf()

    override var uiState = mutableStateListOf<QListView>()
        private set

    var listToSearch = ""

    override fun searchChanged(listToSearch: String) {
        this.listToSearch = listToSearch
        viewModelScope.launch(Dispatchers.IO) {
            val finalList = initialQList.filter {
                it.name.lowercase().contains(listToSearch.lowercase())
            }
            uiState.evenArray(finalList)

            uiState.sortBy {
                it.id
            }
        }
    }

    override fun favouriteClicked(listToUpdate: QListView) {
        viewModelScope.launch(Dispatchers.IO) {
            val mutableListToUpdate = initialQList.find {
                listToUpdate.id == it.id
            }
            mutableListToUpdate!!.isFavourite.value = mutableListToUpdate.isFavourite.value.not()
            updateListUseCase.invoke(QListViewImpl.to(mutableListToUpdate)).collect {
                // val favouritePosition = uiState.indexOf(listToUpdate)
                // uiState[favouritePosition].update(QListCompose.from(it))
            }
        }
    }

    override val updateList: Unit by lazy {

        viewModelScope.launch((Dispatchers.IO)) {
            getListsUseCase().collect {
                Log.d("MainViewModel", "List updated")
                val updateListCompose = QListViewImpl.from(it)
                initialQList.evenArray(updateListCompose)
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
    fun favouriteClicked(it: QListView)

    val updateList: Unit

    val uiState: SnapshotStateList<QListView>
}
