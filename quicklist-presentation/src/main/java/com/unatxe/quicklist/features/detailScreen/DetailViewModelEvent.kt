package com.unatxe.quicklist.features.detailScreen

import com.unatxe.quicklist.entities.QList.item.QListItemViewCheckBox

sealed class DetailViewModelEvent {
    class FocusRequest(
        val qListItemCheckBox: QListItemViewCheckBox
    ) : DetailViewModelEvent()

    class EditRequest(
        val qListItemCheckBox: QListItemViewCheckBox? = null
    ) : DetailViewModelEvent()

    class ListItemValueChange(
        val itemCheckBox: QListItemViewCheckBox,
        val textChanged: String
    ) : DetailViewModelEvent()

    object DoneClicked : DetailViewModelEvent()

    object BackClicked : DetailViewModelEvent()

    class CheckBoxChange(val qListItem: QListItemViewCheckBox) : DetailViewModelEvent()

    object FavouriteClicked : DetailViewModelEvent()
}
