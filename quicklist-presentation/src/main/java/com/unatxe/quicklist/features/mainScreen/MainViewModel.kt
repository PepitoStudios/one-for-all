package com.unatxe.quicklist.features.mainScreen

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.*
import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.domain.repository.QListRepository
import com.unatxe.quicklist.helpers.even
import com.unatxe.quicklist.navigation.NavigationDirections
import com.unatxe.quicklist.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Provider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*

@HiltViewModel
class MainViewModel @Inject constructor(
    private val qListRepository: Provider<QListRepository>,
    private val navigationManager: NavigationManager
    ) : ViewModel(), IMainViewModel {
    override fun listClicked(it: Int?) {
        Log.d("MainViewModel", "List clicked with id: $it")
        navigationManager.navigate(NavigationDirections.ListScreen.listScreen(it))
    }

    private val initialQList: MutableList<QList> = mutableListOf();

    override var uiState = mutableStateListOf<QList>()
        private set;


    override fun searchChanged(listToSearch: String) {

        viewModelScope.launch(Dispatchers.IO){
            val finalList = initialQList.filter {
                it.name.lowercase().contains(listToSearch.lowercase())
            }
            uiState.even(finalList)

            uiState.sortBy {
                it.id
            }
        }
    }

    override val updateList: Unit by lazy {
        viewModelScope.launch {
            qListRepository.get().getLists().collect {
                initialQList.clear()
                initialQList.addAll(it)
                uiState.addAll(it)
            }
        }
        Unit
    }
}

interface IMainViewModel {
    fun listClicked(it: Int?);
    fun searchChanged(listToSearch: String)

    val updateList : Unit;

    val uiState: SnapshotStateList<QList>
}
