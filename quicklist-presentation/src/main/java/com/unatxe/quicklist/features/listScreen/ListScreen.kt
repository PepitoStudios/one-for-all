package com.unatxe.quicklist.features.listScreen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.R
import com.unatxe.quicklist.components.QListCheckBox
import com.unatxe.quicklist.entities.QListItemType
import com.unatxe.quicklist.entities.numberOfCheckedItems
import com.unatxe.quicklist.ui.theme.One4allTheme
import com.unatxe.quicklist.ui.theme.h3Medium

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ListScreen(viewModel: IListViewModel) {
    val uiState = remember {
        viewModel.uiState
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = h3Medium
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors()

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
                    ItemListComponent(
                        uiState = uiState
                    ) {
                        viewModel.onCheckBoxChange(it)
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemListComponent(
    uiState: SnapshotStateList<QListItemType>,
    onCheckBoxChange: (QListItemType.QListItemCheckBox) -> Unit
) {
    var uncheckedVisible by remember { mutableStateOf(true) }

    LazyColumn() {
        items(
            items = uiState,
            key = { it.guid }
        ) { qLisItem ->

            val modifier = Modifier.animateItemPlacement()
            when (qLisItem) {
                is QListItemType.QListItemCheckBox -> {
                    QListCheckBox(
                        modifier = modifier,
                        text = qLisItem.text,
                        checked = qLisItem.checked.value,
                        onCheckBoxCheckedChange = {
                            onCheckBoxChange(qLisItem)
                        }
                    )
                }
                QListItemType.QListItemDoneTitle -> {
                    DoneComponent(
                        text = stringResource(id = R.string.done),
                        checkedItems = uiState.numberOfCheckedItems(),
                        modifier = modifier.clickable {
                            uncheckedVisible = !uncheckedVisible
                        }
                    ) {
                    }
                }
            }
        }
    }
}

@Composable
fun DoneComponent(text: String, checkedItems: Int, modifier: Modifier, onDoneClicked: () -> Unit) {
    Text(
        modifier = modifier,
        text = if (checkedItems > 0) {
            "$text ($checkedItems)"
        } else {
            text
        }

    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListScreenPreview() {
    One4allTheme {
        ListScreen(object : IListViewModel {
            override fun doneClicked() {
                TODO("Not yet implemented")
            }

            override fun onCheckBoxChange(qLisItem: QListItemType.QListItemCheckBox) {
                TODO("Not yet implemented")
            }

            override val uiState: SnapshotStateList<QListItemType>
                get() = SnapshotStateList<QListItemType>()

            override val isCreationMode: Boolean
                get() = false
        })
    }
}
