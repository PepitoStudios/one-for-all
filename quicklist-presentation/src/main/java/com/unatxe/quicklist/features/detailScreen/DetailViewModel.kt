package com.unatxe.quicklist.features.detailScreen

import com.unatxe.quicklist.entities.qList.QListView
import kotlinx.coroutines.flow.StateFlow

interface DetailViewModel {
    val viewState: StateFlow<QListView>
    fun onEvent(detailViewModelEvent: DetailViewModelEvent)
}
