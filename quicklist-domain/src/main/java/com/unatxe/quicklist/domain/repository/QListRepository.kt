package com.unatxe.quicklist.domain.repository

import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.domain.entities.QListItem
import kotlinx.coroutines.flow.Flow

interface QListRepository {

    fun getList(id: Int): Flow<QList>

    fun getLists(): Flow<List<QList>>

    fun insertList(qList: QList): Flow<QList>
    fun deleteList(qList: QList): Flow<Int>
    fun updateList(qList: QList): Flow<QList>

    fun insertListItem(qListItem: QListItem): Flow<QListItem>
    fun deleteListItem(qListItem: QListItem): Flow<Int>
    fun updateListItem(qListItem: QListItem): Flow<QListItem>
}
