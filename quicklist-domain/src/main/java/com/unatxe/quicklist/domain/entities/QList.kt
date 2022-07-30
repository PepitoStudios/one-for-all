package com.unatxe.quicklist.domain.entities

data class QList(
    val id: Int = 0,
    val name: String,
    val isFavourite: Boolean,
    val items: MutableList<QListItem> = mutableListOf()
) {
    fun addItem(item: QListItem) {
        items.add(item)
    }
    fun addItems(qListItems: List<QListItem>) {
        items.addAll(qListItems)
    }
}
