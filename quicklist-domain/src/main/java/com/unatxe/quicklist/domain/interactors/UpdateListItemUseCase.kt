package com.unatxe.quicklist.domain.interactors

import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.domain.entities.QListItem
import com.unatxe.quicklist.domain.repository.QListRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UpdateListItemUseCase @Inject constructor(
    private val qListRepository: QListRepository
) {
    operator fun invoke(qListItem: QListItem): Flow<QListItem> {
        return qListRepository.updateListItem(qListItem)
    }
}
