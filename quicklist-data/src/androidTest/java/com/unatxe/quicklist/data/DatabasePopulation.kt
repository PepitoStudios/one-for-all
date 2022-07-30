package com.unatxe.quicklist.data

import com.unatxe.quicklist.data.entities.QListData
import com.unatxe.quicklist.data.entities.QListItemData

object DatabasePopulation {

    fun populateDbTest(db: QuickListDatabase) {
        db.qListDao().insert(QListData(1, "Lista 1"))
        db.qListDao().insert(QListData(2, "Lista 3"))
        db.qListDao().insert(QListData(3, "Lista 2"))
        db.qListDao().insert(QListData(4, "Lista 4"))
        db.qListDao().insert(QListData(5, "Lista 6"))

        db.qListDao().insert(QListItemData(1, "Item 1", false, 1))
        db.qListDao().insert(QListItemData(2, "Item 2", true, 1))
        db.qListDao().insert(QListItemData(3, "Item 3", false, 1))
    }
}
