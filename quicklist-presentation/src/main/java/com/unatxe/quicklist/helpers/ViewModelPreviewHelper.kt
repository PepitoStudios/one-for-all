package com.unatxe.quicklist.helpers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.unatxe.quicklist.entities.qList.QListView
import com.unatxe.quicklist.entities.qList.QListViewImpl
import com.unatxe.quicklist.features.SummaryScreen.IMainViewModel
import org.joda.time.DateTime

object ViewModelPreviewHelper {

    val previewMainViewModel: () -> IMainViewModel = {
        val mutableLiveQList = mutableStateListOf<QListView>().also {
            it.add(
                QListViewImpl(
                    id = 1,
                    name = "Lista 1",
                    isFavourite = mutableStateOf(true),
                    items = mutableStateListOf(),
                    createdAt = DateTime(),
                    updatedAt = mutableStateOf(DateTime())
                )
            )
            it.add(
                QListViewImpl(
                    id = 2,
                    name = "Lista 2",
                    isFavourite = mutableStateOf(true),
                    items = mutableStateListOf(),
                    createdAt = DateTime(),
                    updatedAt = mutableStateOf(DateTime())
                )
            )
            it.add(
                QListViewImpl(
                    id = 3,
                    name = "Lista 3",
                    isFavourite = mutableStateOf(true),
                    items = mutableStateListOf(),
                    createdAt = DateTime(),
                    updatedAt = mutableStateOf(DateTime())
                )
            )
            it.add(
                QListViewImpl(
                    id = 4,
                    name = "Lista 4",
                    isFavourite = mutableStateOf(true),
                    items = mutableStateListOf(),
                    createdAt = DateTime(),
                    updatedAt = mutableStateOf(DateTime())
                )
            )
            it.add(
                QListViewImpl(
                    id = 5,
                    name = "Lista 5",
                    isFavourite = mutableStateOf(true),
                    items = mutableStateListOf(),
                    createdAt = DateTime(),
                    updatedAt = mutableStateOf(DateTime())
                )
            )
        }
        val previewMainViewModel: () -> IMainViewModel = {

            object : IMainViewModel {
                override fun listClicked(it: Int?) {}
                override fun searchChanged(listToSearch: String) {}
                override fun favouriteClicked(it: QListView) {}

                override val updateList: Unit
                    get() = Unit

                override val uiState: SnapshotStateList<QListView>
                    get() = mutableLiveQList
            }
        }
        previewMainViewModel()
    }
}
