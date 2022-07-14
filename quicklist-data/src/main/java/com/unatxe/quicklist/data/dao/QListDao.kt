package com.unatxe.quicklist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.unatxe.quicklist.data.entities.QListData

@Dao
interface QListDao {

    @Query("SELECT * FROM list_table WHERE id == :idList")
    fun get(idList: Int): QListData

    @Query("SELECT * FROM list_table")
    fun getAll(): List<QListData>

    @Insert
    fun insert(qListData: QListData): Long

    @Delete
    fun delete(qListData: QListData): Int

    @Update
    fun update(qListData: QListData): Int
}
