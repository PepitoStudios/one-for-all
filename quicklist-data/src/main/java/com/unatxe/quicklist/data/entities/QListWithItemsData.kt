package com.unatxe.quicklist.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class QListWithItemsData(
    @Embedded val list: QListData,
    @Relation(
        parentColumn = "id",
        entityColumn = "listId"
    )
    val qListItems: List<QListItemData> = emptyList()
)
