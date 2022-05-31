package com.unatxe.quicklist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unatxe.quicklist.data.dao.QListDao
import com.unatxe.quicklist.data.entities.QList

@Database(entities = [QList::class], version = 1)
abstract class QuickListDatabase : RoomDatabase() {
    abstract fun qListDao(): QListDao
}
