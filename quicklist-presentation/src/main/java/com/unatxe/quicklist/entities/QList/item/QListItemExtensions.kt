package com.unatxe.quicklist.entities.qList.item

private const val ITEM_BEFORE = -1
private const val ITEM_AFTER = 1
private const val ITEM_EQUALS = 0

fun MutableList<QListItemView>.sortPositions() {
    sortWith(
        Comparator { item1, item2 ->
            when (item1) {
                is QListItemViewCheckBoxImpl -> {
                    return@Comparator whenItem1IsQListItemCheckBox(item1, item2)
                }
                is QListItemViewDone -> {
                    when (item2) {
                        is QListItemViewCheckBoxImpl -> {
                            if (item2.checked.value) {
                                return@Comparator ITEM_BEFORE
                            } else {
                                return@Comparator ITEM_AFTER
                            }
                        }
                        is QListItemViewDone -> {
                            return@Comparator ITEM_EQUALS
                        }
                    }
                }
            }
        }
    )
}

private fun whenItem1IsQListItemCheckBox(
    item1: QListItemViewCheckBoxImpl,
    item2: QListItemView
): Int {
    return when (item2) {
        is QListItemViewCheckBoxImpl -> {
            whenAreQListItemCheckBox(item1, item2)
        }
        is QListItemViewDone -> {
            if (item1.checked.value) {
                ITEM_AFTER
            } else {
                ITEM_BEFORE
            }
        }
    }
}

private fun whenAreQListItemCheckBox(
    item1: QListItemViewCheckBoxImpl,
    item2: QListItemViewCheckBoxImpl
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
    item1: QListItemViewCheckBoxImpl,
    item2: QListItemViewCheckBoxImpl
): Int {
    return if (item1.position > item2.position) {
        ITEM_AFTER
    } else {
        ITEM_BEFORE
    }
}
