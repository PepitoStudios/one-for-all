package com.unatxe.quicklist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.unatxe.quicklist.data.entities.QListData
import com.unatxe.quicklist.data.entities.QListItemData
import com.unatxe.quicklist.data.entities.QListWithItemsData

@Dao
interface QListDao {

    @Transaction
    @Query("SELECT * FROM list_table WHERE id == :idList")
    fun get(idList: Int): QListWithItemsData

    @Transaction
    @Query("SELECT * FROM list_table")
    fun getAll(): List<QListWithItemsData>

    @Insert
    fun insert(qListData: QListData): Long

    @Delete
    fun delete(qListData: QListData): Int

    @Query("SELECT * FROM list_items_table WHERE id == :idItem")
    fun getItem(idItem: Int): QListItemData

    @Update
    fun update(qListData: QListData): Int

    @Insert
    fun insert(qListItem: QListItemData): Long

    @Delete
    fun delete(qListItem: QListItemData): Int

    @Update
    fun update(qListItem: QListItemData): Int
}
