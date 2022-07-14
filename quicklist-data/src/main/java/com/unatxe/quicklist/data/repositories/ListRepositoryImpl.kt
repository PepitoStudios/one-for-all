package com.unatxe.quicklist.data.repositories

import com.unatxe.quicklist.data.dao.QListDao
import com.unatxe.quicklist.data.entities.QListData
import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.domain.repository.QListRepository
import com.unatxe.quicklist.domain.utils.Either
import com.unatxe.quicklist.domain.utils.SimpleEither
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ListRepositoryImpl @Inject constructor(val qListDao: () -> QListDao) : QListRepository {
    override fun getHelloWorld(): Flow<SimpleEither<String>> {
        return flow {
            val result = qListDao().getAll().joinToString(separator = ":") { it.name }
            this.emit(Either.success(result))
            Thread.sleep(5000)
            this.emit(Either.success("Iratxe Rules!"))
            Thread.sleep(5000)
            this.emit(Either.success("Kumitxis Rules!"))
        }.flowOn(Dispatchers.IO)
    }

    override fun getList(listId: Int): Flow<QList> {
        return flow {
            val result = qListDao().get(listId)
            this.emit(QList(result.id, result.name))
        }.flowOn(Dispatchers.IO)
    }

    override fun getLists(): Flow<List<QList>> {
        return flow {
            val result = qListDao().getAll()
            this.emit(
                result.map {
                    QList(it.id, it.name)
                }
            )
        }.flowOn(Dispatchers.IO)
    }

    override fun insertList(qList: QList): Flow<QList> {
        return flow {
            val id = qListDao().insert(QListData(name = qList.name))
            val qListData = qListDao().get(id.toInt())
            this.emit(QList(qListData.id, qListData.name))
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
            val id = qListDao().update(QListData(id = 1, name = qList.name))
            val qListData = qListDao().get(id)
            this.emit(QList(qListData.id, qListData.name))
        }.flowOn(Dispatchers.IO)
    }
}
