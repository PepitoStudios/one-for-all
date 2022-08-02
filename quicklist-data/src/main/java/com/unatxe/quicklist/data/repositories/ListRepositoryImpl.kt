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
import kotlinx.coroutines.flow.map
import org.joda.time.DateTime

class ListRepositoryImpl @Inject constructor(val qListDao: () -> QListDao) : QListRepository {

    override fun getList(listId: Int): Flow<QList> {
        return flow {
            val result = qListDao().get(listId)
            val qList = QList(
                id = result.list.id,
                name = result.list.name,
                isFavourite = result.list.isFavourite,
                createdAt = DateTime(result.list.dateCreated),
                updatedAt = DateTime(result.list.dateEdited)
            )
            qList.addItems(
                result.qListItems.map {
                    QListItem(it.id, it.text, it.isChecked, qList)
                }
            )
            this.emit(qList)
        }.flowOn(Dispatchers.IO)
    }

    override fun getLists(): Flow<List<QList>> {
        val result = qListDao().getAll()
            .map {
                it.map { qListWithItemsData ->
                    val qList = QList(
                        id = qListWithItemsData.list.id,
                        name = qListWithItemsData.list.name,
                        isFavourite = qListWithItemsData.list.isFavourite,
                        createdAt = DateTime(qListWithItemsData.list.dateCreated),
                        updatedAt = DateTime(qListWithItemsData.list.dateEdited)
                    )

                    qListWithItemsData.qListItems.let { listItems ->
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
            }.flowOn(Dispatchers.IO)
        return result
    }

    override fun insertList(qList: QList): Flow<QList> {
        return flow {
            val id = qListDao().insert(
                QListData(name = qList.name, isFavourite = qList.isFavourite)
            )
            val qListData = qListDao().get(id.toInt())
            this.emit(
                QList(
                    id = qListData.list.id,
                    name = qListData.list.name,
                    isFavourite = qListData.list.isFavourite,
                    createdAt = DateTime(qListData.list.dateCreated),
                    updatedAt = DateTime(qListData.list.dateEdited)
                )
            )
        }.flowOn(Dispatchers.IO)
    }

    override fun deleteList(qList: QList): Flow<Int> {
        return flow {
            val result = qListDao().delete(QListData(qList.id, qList.name))
            this.emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun updateList(list: QList): Flow<QList> {
        return flow {
            val id = qListDao().update(
                QListData(
                    id = list.id,
                    name = list.name,
                    isFavourite = list.isFavourite,
                    dateCreated = list.createdAt.millis,
                    dateEdited = System.currentTimeMillis()
                )
            )
            val qListData = qListDao().get(list.id)
            val qList = QList(
                id = qListData.list.id,
                name = qListData.list.name,
                isFavourite = qListData.list.isFavourite,
                createdAt = DateTime(qListData.list.dateCreated),
                updatedAt = DateTime(qListData.list.dateEdited)
            )
            qList.addItems(
                qListData.qListItems.map {
                    QListItem(it.id, it.text, it.isChecked, qList)
                }
            )
            emit(qList)
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
