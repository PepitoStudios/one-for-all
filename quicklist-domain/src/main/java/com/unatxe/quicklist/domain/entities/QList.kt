package com.unatxe.quicklist.domain.entities

import org.joda.time.DateTime

data class QList(
    val id: Int = 0,
    val name: String,
    val isFavourite: Boolean,
    val items: MutableList<QListItem> = mutableListOf(),
    val createdAt: DateTime,
    val updatedAt: DateTime
) {
    fun addItem(item: QListItem) {
        items.add(item)
    }
    fun addItems(qListItems: List<QListItem>) {
        items.addAll(qListItems)
    }


}
