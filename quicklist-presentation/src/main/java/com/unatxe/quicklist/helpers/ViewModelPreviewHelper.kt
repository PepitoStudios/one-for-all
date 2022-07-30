package com.unatxe.quicklist.helpers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.features.mainScreen.IMainViewModel

object ViewModelPreviewHelper {

    val previewMainViewModel: () -> IMainViewModel = {
        val mutableLiveQList = mutableStateListOf<QList>().also {
            it.add(QList(1, "Lista 1"))
            it.add(QList(2, "Lista 2"))
            it.add(QList(3, "Lista 3"))
            it.add(QList(4, "Lista 4"))
            it.add(QList(5, "Lista 5"))
        }
        val previewMainViewModel: () -> IMainViewModel = {

            object : IMainViewModel {
                override fun listClicked(it: Int?) {}
                override fun searchChanged(listToSearch: String) {}
                override val updateList: Unit
                    get() = Unit

                override val uiState: SnapshotStateList<QList>
                    get() = mutableLiveQList

            }
        }
        previewMainViewModel();
    }
}
