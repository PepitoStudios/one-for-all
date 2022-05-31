package com.unatxe.quicklist.domain.repository

import com.unatxe.quicklist.domain.utils.SimpleEither
import kotlinx.coroutines.flow.Flow

interface QListRepository {
    fun getHelloWorld(): Flow<SimpleEither<String>>
}
