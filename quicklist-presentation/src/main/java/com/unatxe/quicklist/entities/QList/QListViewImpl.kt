package com.unatxe.quicklist.entities.qList

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.entities.QList.item.QListItemViewCheckBox
import com.unatxe.quicklist.entities.qList.item.QListItemView
import com.unatxe.quicklist.entities.qList.item.QListItemView.Companion.update
import com.unatxe.quicklist.entities.qList.item.QListItemViewCheckBoxImpl
import org.joda.time.DateTime

data class QListViewImpl(
    override val id: Int = 0,
    override val name: String = "",
    override val isFavourite: MutableState<Boolean> = mutableStateOf(false),
    override val items: SnapshotStateList<QListItemView> = mutableStateListOf(),
    override val createdAt: DateTime = DateTime(),
    override val updatedAt: MutableState<DateTime> = mutableStateOf(DateTime()),
    override val isInitialized: MutableState<Boolean> = mutableStateOf(false),
    override val componentSelected: MutableState<QListItemViewCheckBox?> = mutableStateOf(null),
    override val itemIsSelected: MutableState<Boolean> = mutableStateOf(false),
    override val isEditMode: MutableState<Boolean> = mutableStateOf(false),
    override val isCreationMode: MutableState<Boolean> = mutableStateOf(false),
    override val showUncheckedItems: MutableState<Boolean> = mutableStateOf(true),
    override val numCheckedItems: MutableState<Int> = mutableStateOf(0)
) : QListView {

    override fun getTotalAndChecked(): String {
        val checkBox = items.filterIsInstance<QListItemViewCheckBoxImpl>()
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

    fun update(from: QListViewImpl) {
        if (isFavourite.value != from.isFavourite.value) {
            isFavourite.value = from.isFavourite.value
        }
        if (updatedAt.value != from.updatedAt.value) {
            updatedAt.value = from.updatedAt.value
        }

        items.update(from.items)
    }

    override fun equals(other: Any?): Boolean {
        if (other is QListViewImpl && other.id == id) {
            return true
        }
        return false
    }

    companion object {

        fun from(qList: QList): QListViewImpl {
            return QListViewImpl(
                id = qList.id,
                name = qList.name,
                isFavourite = mutableStateOf(qList.isFavourite),
                isInitialized = mutableStateOf(true),
                items = QListItemView.from(qList.items),
                createdAt = qList.createdAt,
                updatedAt = mutableStateOf(qList.updatedAt)
            )
        }

        fun from(qLists: List<QList>): List<QListViewImpl> {
            return qLists.map { from(it) }
        }

        fun to(qListView: QListViewImpl): QList {
            return QList(
                id = qListView.id,
                name = qListView.name,
                isFavourite = qListView.isFavourite.value,
                createdAt = qListView.createdAt,
                updatedAt = qListView.updatedAt.value
            )
        }

        fun List<QListViewImpl>.update(listCompose: List<QListViewImpl>): List<QListViewImpl> {
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
