package com.unatxe.quicklist.entities.QList.item


import androidx.compose.runtime.State

interface QListItemViewCheckBox {
    val id: Int
    val text: State<String>
    val checked: State<Boolean>
    val position: Int
    val idList: Int
    val isCheckBoxEnabled: State<Boolean>
    val isEditMode: State<Boolean>
    val isFocused: State<Boolean>
}
