package com.unatxe.quicklist.data.populations

import com.unatxe.quicklist.data.QuickListDatabase
import com.unatxe.quicklist.data.entities.QList

object DatabasePopulation {

    suspend fun onDatabaseIsEmpty(db: QuickListDatabase) {
        db.qListDao().insert(QList(1, "Lista 1"))
        db.qListDao().insert(QList(2, "Lista 3"))
        db.qListDao().insert(QList(3, "Lista 2"))
        db.qListDao().insert(QList(4, "Lista 4"))
        db.qListDao().insert(QList(5, "Lista 6"))
    }
}
