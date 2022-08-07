package com.unatxe.quicklist.entities

import androidx.compose.runtime.MutableState
import java.util.*

sealed class QListItemType(val guid: String = UUID.randomUUID().toString()) {
    data class QListItemCheckBox(
        val id: Int,
        val text: String,
        val checked: MutableState<Boolean>,
        val position: Int = id
    ) : QListItemType()
    object QListItemDoneTitle : QListItemType()
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
