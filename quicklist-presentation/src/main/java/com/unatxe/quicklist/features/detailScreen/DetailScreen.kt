package com.unatxe.quicklist.features.detailScreen

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.R
import com.unatxe.quicklist.components.QListDetailCheckItemComponent
import com.unatxe.quicklist.components.search.FavouriteIconComponent
import com.unatxe.quicklist.entities.QListCompose
import com.unatxe.quicklist.entities.QListItemType
import com.unatxe.quicklist.features.detailScreen.component.QListDetaulDoneItemComponent
import com.unatxe.quicklist.ui.theme.One4allTheme
import com.unatxe.quicklist.ui.theme.h3Regular

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ListScreen(viewModel: IListViewModel) {
    val uiState = remember {
        viewModel.uiState
    }

    viewModel.initData

    val someQListItemIsFocused = remember { mutableStateOf<QListItemType.QListItemCheckBox?>(null) }

    val currentItemIsFocused = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                modifier = Modifier.shadow(elevation = 10.dp),
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
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
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
                            },
                            onListItemValueChange = { itemCheckBox, value ->
                                (viewModel::onListItemValueChange)(itemCheckBox, value)
                            },
                            onFocusMode = { qListItemCheckBox ->

                                someQListItemIsFocused.value = qListItemCheckBox
                                currentItemIsFocused.value = qListItemCheckBox.isFocused.value || qListItemCheckBox.isEditMode.value
                            }
                        ) {
                            viewModel.doneClicked()
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !currentItemIsFocused.value,
                enter = slideInHorizontally(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    ),
                    initialOffsetX = {
                        it + it / 2
                    }
                    // Expand from the top.

                ),
                exit = slideOutHorizontally(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    ),
                    targetOffsetX = {
                        it + it / 2
                    }
                )
            ) {
                FloatingActionButton(
                    onClick = { viewModel.addItem() }
                ) {
                    Icon(Icons.Filled.Add, stringResource(id = R.string.add_item))
                }
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = currentItemIsFocused.value,
                enter = expandVertically(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        visibilityThreshold = IntSize.VisibilityThreshold
                    ),
                    // Expand from the top.
                    expandFrom = Alignment.Top
                ),
                exit = shrinkVertically(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        visibilityThreshold = IntSize.VisibilityThreshold
                    ),
                    // Expand from the top.
                    shrinkTowards = Alignment.Bottom
                )
            ) {
                BottomAppBar(
                    modifier = Modifier.height(72.dp).shadow(elevation = 10.dp),
                    floatingActionButton = {
                        SmallFloatingActionButton(
                            onClick = {
                                someQListItemIsFocused.value!!.isEditMode.value =
                                    someQListItemIsFocused.value!!.isEditMode.value.not()
                            }
                        ) {
                            Icon(Icons.Filled.Edit, stringResource(id = R.string.edit_item))
                        }
                    },
                    icons = {
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Filled.Check, contentDescription = "Localized description")
                        }
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                )
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
    onListItemValueChange: (QListItemType.QListItemCheckBox, String) -> Unit,
    onFocusMode: (QListItemType.QListItemCheckBox) -> Unit,
    onDoneClick: () -> Unit
) {
    val listState = rememberLazyListState(initialFirstVisibleItemScrollOffset = -1) //

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        item { // Keep this for prevent the first item scrolling behaviour
            Box() {
                Text("")
            }
        }
        items(
            items = uiState,
            key = { it.hashCode() },
            contentType = { it.typeItem }
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
            val onListItemValueChangeRemember = remember {
                onListItemValueChange
            }

            val onFocusChangeRemember = remember {
                onFocusMode
            }

            QListItemColumnComponent(
                modifier,
                qListItemSave,
                numCheckedItemsRemember,
                showUncheckedItemsRemember,
                onCheckBoxChangeRemember,
                onListItemValueChangeRemember,
                onFocusChangeRemember,
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
    onListItemValueChange: (QListItemType.QListItemCheckBox, String) -> Unit,
    onFocusMode: (QListItemType.QListItemCheckBox) -> Unit,
    onDoneClick: () -> Unit
) {
    when (qLisItem.typeItem) {
        QListItemType.QListItemTypeEnum.QListItemCheckBox -> {
            val qListItemCheckBox = qLisItem as QListItemType.QListItemCheckBox
            QListDetailCheckItemComponent(
                modifier = modifier,
                model = qListItemCheckBox,
                onCheckBoxCheckedChange = { onCheckBoxChange(qListItemCheckBox) },
                onListItemValueChange = { onListItemValueChange(qListItemCheckBox, it) },
                onFocusMode = onFocusMode
            )
        }
        QListItemType.QListItemTypeEnum.QListItemDoneTitle -> {
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
            override fun onListItemValueChange(
                qListItem: QListItemType.QListItemCheckBox,
                valueItem: String
            ) {}

            override fun addItem() {}
        })
    }
}
