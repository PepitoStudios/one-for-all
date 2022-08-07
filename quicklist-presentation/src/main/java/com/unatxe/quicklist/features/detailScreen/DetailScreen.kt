package com.unatxe.quicklist.features.detailScreen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.R
import com.unatxe.quicklist.components.QListDetailCheckItemComponent
import com.unatxe.quicklist.components.search.FavouriteIconComponent
import com.unatxe.quicklist.entities.QListCompose
import com.unatxe.quicklist.entities.QListItemType
import com.unatxe.quicklist.features.detailScreen.component.QListDetaulDoneItemComponent
import com.unatxe.quicklist.ui.theme.One4allTheme
import com.unatxe.quicklist.ui.theme.h3Medium
import com.unatxe.quicklist.ui.theme.h3Regular

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ListScreen(viewModel: IListViewModel) {
    val uiState = remember {
        viewModel.uiState
    }

    viewModel.initData

    Scaffold(
        topBar = {
            MediumTopAppBar(

                title = {
                    uiState.value?.name?.let {
                        Text(
                            text = it,
                            style = h3Regular
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onBackClicked()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "Back Previous Page")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                actions = {
                    uiState.value?.let { qListCompose ->
                        FavouriteIconComponent(qListCompose.isFavourite) {
                            viewModel.onFavoriteClick(qListCompose)
                        }
                    }
                }

            )
        },
        content = { padding ->
            Box(
                modifier = Modifier.padding(
                    top = padding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = padding.calculateBottomPadding()
                )
            ) {
                if (viewModel.isCreationMode) {
                    Text("ListScreen without id")
                } else {
                    uiState.value?.let { qListCompose ->
                        QListItemLazyComponent(
                            uiState = qListCompose.items,
                            numCheckedItems = viewModel.numCheckedItems,
                            showUncheckedItems = viewModel.showUncheckedItems,
                            onCheckBoxChange = {
                                (viewModel::onCheckBoxChange)(it)
                            }
                        ) {
                            viewModel.doneClicked()
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QListItemLazyComponent(
    uiState: SnapshotStateList<QListItemType>,
    numCheckedItems: MutableState<Int>,
    showUncheckedItems: MutableState<Boolean>,
    onCheckBoxChange: (QListItemType.QListItemCheckBox) -> Unit,
    onDoneClick: () -> Unit
) {
    LazyColumn() {
        items(
            items = uiState,
            key = { it.guid }
        ) { qLisItem ->

            val modifier = remember { Modifier.animateItemPlacement() }
            val qListItemSave = remember { qLisItem }
            val numCheckedItemsRemember = remember {
                numCheckedItems
            }
            val showUncheckedItemsRemember = remember {
                showUncheckedItems
            }
            val onCheckBoxChangeRemember = remember {
                onCheckBoxChange
            }
            val onDoneClickRemember = remember {
                onDoneClick
            }

            QListItemColumnComponent(
                modifier,
                qListItemSave,
                numCheckedItemsRemember,
                showUncheckedItemsRemember,
                onCheckBoxChangeRemember,
                onDoneClickRemember
            )
        }
    }
}

@Composable
fun QListItemColumnComponent(
    modifier: Modifier,
    qLisItem: QListItemType,
    numCheckedItems: MutableState<Int>,
    showUncheckedItems: MutableState<Boolean>,
    onCheckBoxChange: (QListItemType.QListItemCheckBox) -> Unit,
    onDoneClick: () -> Unit
) {
    when (qLisItem) {
        is QListItemType.QListItemCheckBox -> {
            QListDetailCheckItemComponent(
                modifier = modifier,
                text = qLisItem.text,
                checked = qLisItem.checked,
                onCheckBoxCheckedChange = {
                    onCheckBoxChange(qLisItem)
                }
            )
        }
        QListItemType.QListItemDoneTitle -> {
            val showDone by remember {
                derivedStateOf {
                    numCheckedItems.value > 0
                }
            }

            if (showDone) {
                QListDetaulDoneItemComponent(
                    modifier = Modifier.clickable {
                        onDoneClick()
                    },
                    text = stringResource(id = R.string.done),
                    checkedItems = numCheckedItems,
                    showDoneItems = showUncheckedItems
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListScreenPreview() {
    One4allTheme {
        ListScreen(object : IListViewModel {
            override val uiState: MutableState<QListCompose?>
                get() = mutableStateOf<QListCompose?>(null)

            override val isCreationMode: Boolean
                get() = false
            override var showUncheckedItems: MutableState<Boolean>
                get() = mutableStateOf(true)
                set(value) {}
            override var numCheckedItems: MutableState<Int>
                get() = mutableStateOf(0)
                set(value) {}
            override val initData: Unit
                get() = TODO("Not yet implemented")

            override fun doneClicked() {}

            override fun onCheckBoxChange(qLisItem: QListItemType.QListItemCheckBox) {}

            override fun onBackClicked() {}

            override fun onFavoriteClick(qListCompose: QListCompose) {}
        })
    }
}
