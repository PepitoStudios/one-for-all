package com.unatxe.quicklist.helpers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.unatxe.quicklist.entities.QListCompose
import com.unatxe.quicklist.features.SummaryScreen.IMainViewModel
import org.joda.time.DateTime

object ViewModelPreviewHelper {

    val previewMainViewModel: () -> IMainViewModel = {
        val mutableLiveQList = mutableStateListOf<QListCompose>().also {
            it.add(
                QListCompose(
                    id = 1,
                    name = "Lista 1",
                    isFavourite = mutableStateOf(true),
                    items = mutableStateListOf(),
                    createdAt = DateTime(),
                    updatedAt = mutableStateOf(DateTime())
                )
            )
            it.add(
                QListCompose(
                    id = 2,
                    name = "Lista 2",
                    isFavourite = mutableStateOf(true),
                    items = mutableStateListOf(),
                    createdAt = DateTime(),
                    updatedAt = mutableStateOf(DateTime())
                )
            )
            it.add(
                QListCompose(
                    id = 3,
                    name = "Lista 3",
                    isFavourite = mutableStateOf(true),
                    items = mutableStateListOf(),
                    createdAt = DateTime(),
                    updatedAt = mutableStateOf(DateTime())
                )
            )
            it.add(
                QListCompose(
                    id = 4,
                    name = "Lista 4",
                    isFavourite = mutableStateOf(true),
                    items = mutableStateListOf(),
                    createdAt = DateTime(),
                    updatedAt = mutableStateOf(DateTime())
                )
            )
            it.add(
                QListCompose(
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
                override fun favouriteClicked(it: QListCompose) {}

                override val updateList: Unit
                    get() = Unit

                override val uiState: SnapshotStateList<QListCompose>
                    get() = mutableLiveQList
            }
        }
        previewMainViewModel()
    }
}
