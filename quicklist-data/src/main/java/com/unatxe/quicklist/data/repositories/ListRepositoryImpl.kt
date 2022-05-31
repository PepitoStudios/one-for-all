package com.unatxe.quicklist.data.repositories

import com.unatxe.quicklist.data.dao.QListDao
import com.unatxe.quicklist.domain.repository.QListRepository
import com.unatxe.quicklist.domain.utils.Either
import com.unatxe.quicklist.domain.utils.SimpleEither
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ListRepositoryImpl @Inject constructor(val qListDao: QListDao) : QListRepository {
    override fun getHelloWorld(): Flow<SimpleEither<String>> {
        return flow {
            this.emit(Either.success("Hello yeah!"))
            Thread.sleep(5000)
            this.emit(Either.success("Iratxe Rules!"))
            Thread.sleep(5000)
            this.emit(Either.success("Kumitxis Rules!"))
        }.flowOn(Dispatchers.IO)
    }
}

