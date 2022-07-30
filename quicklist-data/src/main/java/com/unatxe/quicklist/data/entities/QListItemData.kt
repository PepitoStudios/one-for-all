package com.unatxe.quicklist.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_items_table")
data class QListItemData(
    @PrimaryKey(autoGenerate = true)
    public val id: Int = 0,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "checked") val isChecked: Boolean,
    @ColumnInfo(name = "listId") val listId: Int)
