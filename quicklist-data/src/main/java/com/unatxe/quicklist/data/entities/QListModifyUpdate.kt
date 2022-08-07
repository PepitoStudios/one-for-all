package com.unatxe.quicklist.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
class QListModifyUpdate(@ColumnInfo(name = "id") var id: Int) {

    @ColumnInfo(name = "dateEdited")
    val dateEdited: Long = System.currentTimeMillis()
}
