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
    val updatedAt: MutableState<DateTime>
) {

    fun getTotalAndChecked(): String {
        val total = items.size
        val checked = items.filter { it.checked }.size
        return if (total == 0 && checked == 0) {
            "- / -"
        } else {
            "$checked / $total"
        }
    }

    fun update(from: QListCompose) {
        isFavourite.value = from.isFavourite.value
        updatedAt.value = from.updatedAt.value
    }

    override fun equals(other: Any?): Boolean {
        if (other is QListCompose && other.id == id) {
            return true
        }
        return false
    }

    companion object {
        fun from(qList: QList): QListCompose {
            return QListCompose(
                id = qList.id,
                name = qList.name,
                isFavourite = mutableStateOf(qList.isFavourite),
                items = qList.items.toMutableStateList(),
                createdAt = qList.createdAt,
                updatedAt = mutableStateOf(qList.updatedAt)
            )
        }

        fun from(qLists: List<QList>): List<QListCompose> {
            return qLists.map { from(it) }
        }

        fun to(qListCompose: QListCompose): QList {
            return QList(
                id = qListCompose.id,
                name = qListCompose.name,
                isFavourite = qListCompose.isFavourite.value,
                createdAt = qListCompose.createdAt,
                updatedAt = qListCompose.updatedAt.value
            )
        }

        fun List<QListCompose>.update(listCompose: List<QListCompose>): List<QListCompose> {
            listCompose.forEach {
                val index = this.indexOf(it)
                if (index != -1) {
                    this[index].update(it)
                }
            }
            return this
        }
    }
}
