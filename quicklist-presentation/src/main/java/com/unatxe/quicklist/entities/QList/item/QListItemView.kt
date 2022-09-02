package com.unatxe.quicklist.entities.qList.item

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.unatxe.quicklist.domain.entities.QListItem

sealed class QListItemView(
    val typeItem: QListItemTypesEnum
) {
    override fun equals(other: Any?): Boolean {
        if (other is QListItemViewDone && this is QListItemViewDone) {
            return true
        }
        return false
    }
    override fun hashCode(): Int {
        return typeItem.hashCode()
    }
    companion object {
        fun from(items: List<QListItem>):
            SnapshotStateList<QListItemView> {
            val itemsTypes = SnapshotStateList<QListItemView>()
            val sorted = items.sortedBy {
                it.id
            }.map {
                QListItemViewCheckBoxImpl(
                    it.id,
                    mutableStateOf(it.text),
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
            itemsTypes.add(QListItemViewDone)
            itemsTypes.addAll(checkedItems)

            return itemsTypes
        }

        fun to(
            qListItem: QListItemViewCheckBoxImpl
        ): QListItem {
            return QListItem(
                qListItem.id,
                qListItem.text.value,
                qListItem.checked.value,
                qListItem.idList
            )
        }

        fun List<QListItemView>.update(
            listCompose: List<QListItemView>
        ): List<QListItemView> {
            listCompose.forEach { qListItemType ->
                if (qListItemType is QListItemViewCheckBoxImpl) {
                    val item = this.find {
                        it is QListItemViewCheckBoxImpl &&
                            qListItemType.id == it.id &&
                            (
                                qListItemType.checked.value != it.checked.value ||
                                    qListItemType.text.value != it.text.value
                                )
                    } as QListItemViewCheckBoxImpl?
                    item?.let {
                        if (it.checked.value != qListItemType.checked.value) {
                            it.checked.value = qListItemType.checked.value
                        }
                        if (it.text.value != qListItemType.text.value) {
                            it.text.value = qListItemType.text.value
                        }
                    }
                }
            }
            return this
        }
    }
}
