package com.unatxe.quicklist.entities.qList.item

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.unatxe.quicklist.entities.QList.item.QListItemViewCheckBox

data class QListItemViewCheckBoxImpl(
    override val id: Int,
    override val text: MutableState<String>,
    override val checked: MutableState<Boolean>,
    override val position: Int = id,
    override val idList: Int,
    override val isCheckBoxEnabled: MutableState<Boolean> = mutableStateOf(true),
    override val isEditMode: MutableState<Boolean> = mutableStateOf(false),
    override val isFocused: MutableState<Boolean> = mutableStateOf(false)

) : QListItemView(typeItem = QListItemTypesEnum.QListItemCheckBox), QListItemViewCheckBox {
    override fun equals(other: Any?): Boolean {
        if (other is QListItemViewCheckBoxImpl && other.id == id) {
            return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result: Int = id
        result += text.hashCode()
        result += checked.hashCode()
        result += position
        result += idList
        return result
    }
}
