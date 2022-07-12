package com.unatxe.quicklist.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_table")
data class QList(
    @PrimaryKey(autoGenerate = true)private val id: Int,
    @ColumnInfo(name = "name") val name: String,
) {
    public fun getId(): Int {
        return id
    }
}
