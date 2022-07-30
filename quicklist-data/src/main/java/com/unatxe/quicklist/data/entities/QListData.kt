package com.unatxe.quicklist.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_table")
data class QListData(
    @PrimaryKey(autoGenerate = true)
    public val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "favourite") val isFavourite: Boolean = false
)
