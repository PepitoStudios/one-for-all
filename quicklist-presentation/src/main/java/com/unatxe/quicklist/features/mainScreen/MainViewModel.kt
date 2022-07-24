package com.unatxe.quicklist.features.mainScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.domain.repository.QListRepository
import com.unatxe.quicklist.domain.utils.Either
import com.unatxe.quicklist.navigation.NavigationDirections
import com.unatxe.quicklist.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.map

@HiltViewModel
class MainViewModel @Inject constructor(
    private val qListRepository: Provider<QListRepository>,
    private val navigationManager: NavigationManager
    ) : ViewModel() {
    fun listClicked(it: Int?) {
        Log.d("MainViewModel", "List clicked with id: $it")
        navigationManager.navigate(NavigationDirections.ListScreen.listScreen(it))
    }

    val getLists: LiveData<List<QList>> by lazy {
        qListRepository.get().getLists().asLiveData()
    }
}
