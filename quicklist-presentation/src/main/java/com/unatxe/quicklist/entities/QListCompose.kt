package com.unatxe.quicklist.entities

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.entities.QListItemType.Companion.update
import org.joda.time.DateTime

data class QListCompose(
    val id: Int = 0,
    val name: String = "",
    val isFavourite: MutableState<Boolean> = mutableStateOf(false),
    val items: SnapshotStateList<QListItemType> = mutableStateListOf(),
    val createdAt: DateTime = DateTime(),
    val updatedAt: MutableState<DateTime> = mutableStateOf(DateTime()),
    val isInitialized: MutableState<Boolean> = mutableStateOf(false),
    val componentSelected: MutableState<QListItemType.QListItemCheckBox?> = mutableStateOf(null),
    val itemIsSelected: MutableState<Boolean> = mutableStateOf(false),
    val isEditMode: MutableState<Boolean> = mutableStateOf(false)
) {

    fun getTotalAndChecked(): String {
        val checkBox = items.filterIsInstance<QListItemType.QListItemCheckBox>()
        val checked = checkBox.filter {
            it.checked.value
        }.size
        val total = checkBox.size
        return if (total == 0 && checked == 0) {
            "- / -"
        } else {
            "$checked / $total"
        }
    }

    fun update(from: QListCompose) {
        if (isFavourite.value != from.isFavourite.value) {
            isFavourite.value = from.isFavourite.value
        }
        if (updatedAt.value != from.updatedAt.value) {
            updatedAt.value = from.updatedAt.value
        }

        items.update(from.items)
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
                isInitialized = mutableStateOf(true),
                items = QListItemType.from(qList.items),
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
