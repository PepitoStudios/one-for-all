package com.unatxe.quicklist.features.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.unatxe.quicklist.domain.repository.QListRepository
import com.unatxe.quicklist.domain.utils.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.map

@HiltViewModel
class MainViewModel @Inject constructor
(private val qListRepository: Provider<QListRepository>) : ViewModel() {

    val hello: LiveData<String> by lazy {
        qListRepository.get().getHelloWorld()
            .map {
                (it as Either.Success).value
            }.asLiveData()
    }
}
