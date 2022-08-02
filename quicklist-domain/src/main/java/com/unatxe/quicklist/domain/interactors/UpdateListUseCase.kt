package com.unatxe.quicklist.domain.interactors

import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.domain.repository.QListRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UpdateListUseCase @Inject constructor(
    private val qListRepository: QListRepository
) {
    operator fun invoke(qList: QList): Flow<QList> {
        return qListRepository.updateList(qList)
    }
}
