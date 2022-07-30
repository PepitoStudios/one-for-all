package com.unatxe.quicklist.data.repositories

import com.unatxe.quicklist.data.dao.QListDao
import com.unatxe.quicklist.data.entities.QListData
import com.unatxe.quicklist.data.entities.QListItemData
import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.domain.entities.QListItem
import com.unatxe.quicklist.domain.repository.QListRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ListRepositoryImpl @Inject constructor(val qListDao: () -> QListDao) : QListRepository {

    override fun getList(listId: Int): Flow<QList> {
        return flow {
            val result = qListDao().get(listId)
            val qList = QList(result.list.id, result.list.name, result.list.isFavourite)
            qList.addItems(
                result.qListItems.map {
                    QListItem(it.id, it.text, it.isChecked, qList)
                }
            )
            this.emit(qList)
        }.flowOn(Dispatchers.IO)
    }

    override fun getLists(): Flow<List<QList>> {
        return flow {
            val result = qListDao().getAll()
            this.emit(
                result.map {
                    val qList = QList(it.list.id, it.list.name, it.list.isFavourite)

                    it.qListItems.let { listItems ->
                        qList.addItems(
                            listItems.map { qListItemData ->
                                QListItem(
                                    qListItemData.id,
                                    qListItemData.text,
                                    qListItemData.isChecked,
                                    qList
                                )
                            }
                        )
                    }
                    qList
                }
            )
        }.flowOn(Dispatchers.IO)
    }

    override fun insertList(qList: QList): Flow<QList> {
        return flow {
            val id = qListDao().insert(
                QListData(name = qList.name, isFavourite = qList.isFavourite)
            )
            val qListData = qListDao().get(id.toInt())
            this.emit(QList(qListData.list.id, qListData.list.name, qListData.list.isFavourite))
        }.flowOn(Dispatchers.IO)
    }

    override fun deleteList(qList: QList): Flow<Int> {
        return flow {
            val result = qListDao().delete(QListData(qList.id, qList.name))
            this.emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun updateList(qList: QList): Flow<QList> {
        return flow {
            val id = qListDao().update(
                QListData(id = 1, name = qList.name, isFavourite = qList.isFavourite)
            )
            val qListData = qListDao().get(id)
            this.emit(QList(qListData.list.id, qListData.list.name, qListData.list.isFavourite))
        }.flowOn(Dispatchers.IO)
    }

    override fun insertListItem(qListItem: QListItem): Flow<QListItem> {
        return flow {
            val id = qListDao().insert(
                QListItemData(
                    text = qListItem.text,
                    isChecked = qListItem.checked,
                    listId = qListItem.qList.id
                )
            )

            val qListData = qListDao().getItem(id.toInt())

            this.emit(
                QListItem(
                    id = qListData.id,
                    text = qListData.text,
                    checked = qListData.isChecked,
                    qList = qListItem.qList
                )
            )
        }.flowOn(Dispatchers.IO)
    }

    override fun deleteListItem(qListItem: QListItem): Flow<Int> {
        return flow {
            val result = qListDao().delete(
                QListItemData(
                    id = qListItem.id,
                    text = qListItem.text,
                    isChecked = qListItem.checked,
                    listId = qListItem.qList.id
                )
            )
            this.emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun updateListItem(qListItem: QListItem): Flow<QListItem> {
        return flow {
            val id = qListDao().update(
                QListItemData(
                    id = qListItem.id,
                    text = qListItem.text,
                    isChecked = qListItem.checked,
                    listId = qListItem.qList.id
                )
            )

            val qListItemRetrieve = qListDao().getItem(id)
            this.emit(
                QListItem(
                    id = qListItemRetrieve.id,
                    text = qListItemRetrieve.text,
                    checked = qListItemRetrieve.isChecked,
                    qList = qListItem.qList
                )
            )
        }.flowOn(Dispatchers.IO)
    }
}
