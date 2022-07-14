package com.unatxe.quicklist.domain.repository

import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.domain.utils.SimpleEither
import kotlinx.coroutines.flow.Flow

interface QListRepository {
    fun getHelloWorld(): Flow<SimpleEither<String>>

    fun getList(id: Int): Flow<QList>

    fun getLists(): Flow<List<QList>>

    fun insertList(qList: QList): Flow<QList>

    fun deleteList(qList: QList): Flow<Int>
    fun updateList(qList: QList): Flow<QList>
}
