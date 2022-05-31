package com.unatxe.quicklist.features.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.unatxe.quicklist.domain.repository.QListRepository
import com.unatxe.quicklist.domain.utils.Either
import com.unatxe.quicklist.domain.utils.Either.Companion.success
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class MainViewModel @Inject constructor(val qListRepository: QListRepository) : ViewModel() {

    public val hello = qListRepository.getHelloWorld()
        .map {
            (it as Either.Success).value
        }.asLiveData()
}
