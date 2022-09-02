package com.unatxe.quicklist.features.detailScreen.component

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.R
import com.unatxe.quicklist.components.QListDetailCheckItemComponent
import com.unatxe.quicklist.entities.qList.QListView
import com.unatxe.quicklist.entities.qList.item.QListItemTypesEnum
import com.unatxe.quicklist.entities.qList.item.QListItemView
import com.unatxe.quicklist.entities.qList.item.QListItemViewCheckBoxImpl
import com.unatxe.quicklist.features.detailScreen.DetailViewModelEvent
import kotlinx.coroutines.delay

@Composable
fun QListItemLazyComponent(
    modifier: Modifier,
    uiState: QListView,
    eventEmitter: (event: DetailViewModelEvent) -> Unit
) {
    val listState = rememberLazyListState() //

    if (uiState.componentSelected.value != null) {
        Log.d("Test", "Selected Mode ${uiState.componentSelected.value!!.text}")
        LaunchedEffect(Unit) {
            delay(100)
            val index = uiState.items.indexOfFirst {
                it == uiState.componentSelected.value
            }
            listState.scrollToItem(index)
        }
    }

    if (uiState.isEditMode.value) {
        LaunchedEffect(Unit) {
            delay(100)
            val index = uiState.items.indexOfFirst {
                it == uiState.componentSelected.value
            }
            listState.scrollToItem(index)
        }
    }

    LazyColumnComponent(
        listState,
        modifier,
        uiState,
        eventEmitter

    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyColumnComponent(
    listState: LazyListState,
    modifier: Modifier,
    uiState: QListView,
    eventEmitter: (event: DetailViewModelEvent) -> Unit

) {
    val uiStateRemember = remember {
        uiState
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        userScrollEnabled = uiState.isEditMode.value.not()
    ) {
        item { // Keep this for prevent the first item scrolling behaviour
            Box() {
                Text("")
            }
        }
        items(
            items = uiState.items,
            key = { it.hashCode() },
            contentType = { it.typeItem }
        ) { qLisItem ->

            val animateComponentModifier = remember { Modifier.animateItemPlacement() }

            val qListItemRemember = remember {
                qLisItem
            }

            QListItemColumnComponent(
                animateComponentModifier,
                qListItemRemember,
                uiStateRemember,
                eventEmitter
            )
        }

        item {
            BoxWithConstraints(modifier = Modifier.height(400.dp)) {
                Image(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    alignment = Alignment.BottomCenter,
                    painter = painterResource(id = R.drawable.item_list_empty),
                    contentDescription = stringResource(
                        id = R.string.item_list_empty
                    )
                )
            }
        }
    }
}

@Composable
fun QListItemColumnComponent(
    modifier: Modifier,
    qLisItem: QListItemView,
    uiState: QListView,
    eventEmitter: (event: DetailViewModelEvent) -> Unit
) {
    when (qLisItem.typeItem) {
        QListItemTypesEnum.QListItemCheckBox -> {
            val qListItemCheckBox = qLisItem as QListItemViewCheckBoxImpl
            QListDetailCheckItemComponent(
                modifier = modifier,
                model = qListItemCheckBox,
                eventEmitter = eventEmitter
            )
        }

        QListItemTypesEnum.QListItemDoneTitle -> {
            val showDone by remember {
                derivedStateOf {
                    uiState.numCheckedItems.value > 0
                }
            }

            if (showDone) {
                QListDefaultDoneItemComponent(
                    modifier = Modifier.clickable {
                        eventEmitter.invoke(DetailViewModelEvent.DoneClicked)
                    }.padding(start = 16.dp),
                    text = stringResource(id = R.string.done),
                    checkedItems = uiState.numCheckedItems,
                    showDoneItems = uiState.showUncheckedItems
                )
            }
        }
    }
}
