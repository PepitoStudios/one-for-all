package com.unatxe.quicklist.helpers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.features.mainScreen.IMainViewModel
import org.joda.time.DateTime

object ViewModelPreviewHelper {

    val previewMainViewModel: () -> IMainViewModel = {
        val mutableLiveQList = mutableStateListOf<QList>().also {
            it.add(
                QList(
                    id = 1,
                    name = "Lista 1",
                    isFavourite = true,
                    createdAt = DateTime(),
                    updatedAt = DateTime()
                )
            )
            it.add(
                QList(
                    id = 2,
                    name = "Lista 2",
                    isFavourite = true,
                    createdAt = DateTime(),
                    updatedAt = DateTime()
                )
            )
            it.add(
                QList(
                    id = 3,
                    name = "Lista 3",
                    isFavourite = true,
                    createdAt = DateTime(),
                    updatedAt = DateTime()
                )
            )
            it.add(
                QList(
                    id = 4,
                    name = "Lista 4",
                    isFavourite = true,
                    createdAt = DateTime(),
                    updatedAt = DateTime()
                )
            )
            it.add(
                QList(
                    id = 5,
                    name = "Lista 5",
                    isFavourite = true,
                    createdAt = DateTime(),
                    updatedAt = DateTime()
                )
            )
        }
        val previewMainViewModel: () -> IMainViewModel = {

            object : IMainViewModel {
                override fun listClicked(it: Int?) {}
                override fun searchChanged(listToSearch: String) {}
                override fun favouriteClicked(it: QList) {
                }

                override val updateList: Unit
                    get() = Unit

                override val uiState: SnapshotStateList<QList>
                    get() = mutableLiveQList
            }
        }
        previewMainViewModel()
    }
}
