package com.unatxe.quicklist.entities.qList

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.unatxe.quicklist.entities.QList.item.QListItemViewCheckBox
import com.unatxe.quicklist.entities.qList.item.QListItemView
import org.joda.time.DateTime

@Immutable
interface QListView {
    val id: Int
    val name: String
    val isFavourite: State<Boolean>
    val items: SnapshotStateList<QListItemView>
    val createdAt: DateTime
    val updatedAt: State<DateTime>
    val isInitialized: State<Boolean>
    val componentSelected: State<QListItemViewCheckBox?>
    val itemIsSelected: State<Boolean>
    val isEditMode: State<Boolean>
    val isCreationMode: State<Boolean>
    val showUncheckedItems: State<Boolean>
    val numCheckedItems: State<Int>

    fun getTotalAndChecked(): String;
}
