package com.unatxe.quicklist.entities

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.domain.entities.QListItem
import org.joda.time.DateTime

data class QListCompose(
    val id: Int = 0,
    val name: String,
    val isFavourite: MutableState<Boolean>,
    val items: SnapshotStateList<QListItem>,
    val createdAt: DateTime,
    val updatedAt: DateTime
) {

    fun getTotalAndChecked(): Pair<Int, Int> {
        return Pair(items.size, items.filter { it.checked }.size)
    }
    companion object {
        fun from(qList: QList): QListCompose {
            return QListCompose(
                id = qList.id,
                name = qList.name,
                isFavourite = mutableStateOf(qList.isFavourite),
                items = qList.items.toMutableStateList(),
                createdAt = qList.createdAt,
                updatedAt = qList.updatedAt
            )
        }

        fun from(qLists: List<QList>): List<QListCompose> {
            return qLists.map { from(it) }
        }
    }
}
