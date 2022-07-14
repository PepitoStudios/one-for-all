package com.unatxe.quicklist.data

import com.unatxe.quicklist.data.entities.QListData

object DatabasePopulation {

    fun populateDbTest(db: QuickListDatabase) {
        db.qListDao().insert(QListData(1, "Lista 1"))
        db.qListDao().insert(QListData(2, "Lista 3"))
        db.qListDao().insert(QListData(3, "Lista 2"))
        db.qListDao().insert(QListData(4, "Lista 4"))
        db.qListDao().insert(QListData(5, "Lista 6"))
    }
}
