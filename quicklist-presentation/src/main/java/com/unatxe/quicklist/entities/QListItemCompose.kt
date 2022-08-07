package com.unatxe.quicklist.entities

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.unatxe.quicklist.domain.entities.QListItem
import java.util.*

sealed class QListItemType(val guid: String = UUID.randomUUID().toString()) {

    override fun equals(other: Any?): Boolean {
        if (other is QListItemDoneTitle && this is QListItemDoneTitle) {
            return true
        }
        return false
    }

    data class QListItemCheckBox(
        val id: Int,
        val text: String,
        val checked: MutableState<Boolean>,
        val position: Int = id,
        val idList: Int
    ) : QListItemType() {
        override fun equals(other: Any?): Boolean {
            if (other is QListItemCheckBox && other.id == id) {
                return true
            }
            return false
        }
    }
    object QListItemDoneTitle : QListItemType()

    companion object {
        fun from(items: List<QListItem>): SnapshotStateList<QListItemType> {
            val itemsTypes = SnapshotStateList<QListItemType>()
            val sorted = items.sortedBy {
                it.id
            }.map {
                QListItemType.QListItemCheckBox(
                    it.id,
                    it.text,
                    mutableStateOf(it.checked),
                    it.id,
                    it.idList
                )
            }
            val uncheckedItems = sorted.filter {
                it.checked.value.not()
            }

            val checkedItems = sorted.filter {
                it.checked.value
            }

            itemsTypes.addAll(uncheckedItems)
            itemsTypes.add(QListItemType.QListItemDoneTitle)
            itemsTypes.addAll(checkedItems)

            return itemsTypes
        }

        public fun to(
            qListItem: QListItemType.QListItemCheckBox
        ): QListItem {
            return QListItem(
                qListItem.id,
                qListItem.text,
                qListItem.checked.value,
                qListItem.idList
            )
        }

        fun List<QListItemType>.update(listCompose: List<QListItemType>): List<QListItemType> {
            listCompose.forEach { qListItemType ->
                if (qListItemType is QListItemCheckBox) {
                    val item = this.find {
                        it is QListItemCheckBox && qListItemType.id == it.id
                    } as QListItemCheckBox
                    item.checked.value = qListItemType.checked.value
                }
            }
            return this
        }
    }
}

const val ITEM_BEFORE = -1
const val ITEM_AFTER = 1
const val ITEM_EQUALS = 0

fun List<QListItemType>.numberOfCheckedItems(): Int {
    return this.count { it is QListItemType.QListItemCheckBox && it.checked.value }
}

fun MutableList<QListItemType>.sortPositions() {
    sortWith(
        Comparator { item1, item2 ->
            when (item1) {
                is QListItemType.QListItemCheckBox -> {
                    return@Comparator whenItem1IsQListItemCheckBox(item1, item2)
                }
                is QListItemType.QListItemDoneTitle -> {
                    when (item2) {
                        is QListItemType.QListItemCheckBox -> {
                            if (item2.checked.value) {
                                return@Comparator ITEM_BEFORE
                            } else {
                                return@Comparator ITEM_AFTER
                            }
                        }
                        is QListItemType.QListItemDoneTitle -> {
                            return@Comparator ITEM_EQUALS
                        }
                    }
                }
            }
        }
    )
}

private fun whenItem1IsQListItemCheckBox(
    item1: QListItemType.QListItemCheckBox,
    item2: QListItemType
): Int {
    return when (item2) {
        is QListItemType.QListItemCheckBox -> {
            whenAreQListItemCheckBox(item1, item2)
        }
        is QListItemType.QListItemDoneTitle -> {
            if (item1.checked.value) {
                ITEM_AFTER
            } else {
                ITEM_BEFORE
            }
        }
    }
}

private fun whenAreQListItemCheckBox(
    item1: QListItemType.QListItemCheckBox,
    item2: QListItemType.QListItemCheckBox
): Int {
    return if (item1.checked.value && item2.checked.value) {
        whenBothAreCheckedOrUnchecked(item1, item2)
    } else if (item1.checked.value) {
        ITEM_AFTER
    } else if (item2.checked.value) {
        ITEM_BEFORE
    } else {
        whenBothAreCheckedOrUnchecked(item1, item2)
    }
}

private fun whenBothAreCheckedOrUnchecked(
    item1: QListItemType.QListItemCheckBox,
    item2: QListItemType.QListItemCheckBox
): Int {
    return if (item1.position > item2.position) {
        ITEM_AFTER
    } else {
        ITEM_BEFORE
    }
}
