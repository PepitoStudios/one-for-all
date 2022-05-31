package com.unatxe.quicklist.domain.interactors

import com.unatxe.quicklist.domain.repository.QListRepository
import com.unatxe.quicklist.domain.utils.SimpleEither
import kotlinx.coroutines.flow.Flow

inline fun getHelloWorld(
    qListRepository: QListRepository
): Flow<SimpleEither<String>> {
    return qListRepository.getHelloWorld()
}

typealias GetHelloWorldInteractor = () -> Flow<SimpleEither<String>>
