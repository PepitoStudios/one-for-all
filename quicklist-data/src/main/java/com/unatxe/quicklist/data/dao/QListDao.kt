package com.unatxe.quicklist.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.unatxe.quicklist.data.entities.QList

@Dao
interface QListDao {
    @Query("SELECT * FROM list_table")
    fun getAll(): List<QList>

    @Insert
    fun insert(vararg users: QList)

}
