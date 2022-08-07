package com.unatxe.quicklist.features.listScreen

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.R
import com.unatxe.quicklist.components.QListCheckBox
import com.unatxe.quicklist.entities.QListItemType
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
                        uiState = uiState,
                        numCheckedItems = viewModel.numCheckedItems.value,
                        showUncheckedItems = viewModel.showUncheckedItems.value,
                        onCheckBoxChange = {
                            viewModel.onCheckBoxChange(it)
                        },
                        onDoneClick = {
                            viewModel.doneClicked()
                        }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemListComponent(
    uiState: SnapshotStateList<QListItemType>,
    numCheckedItems: Int,
    showUncheckedItems: Boolean,
    onCheckBoxChange: (QListItemType.QListItemCheckBox) -> Unit,
    onDoneClick: () -> Unit
) {
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
                    if (numCheckedItems > 0) {
                        DoneComponent(
                            modifier = modifier.clickable {
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
    }
}

@Composable
fun DoneComponent(modifier: Modifier, text: String, checkedItems: Int, showDoneItems: Boolean) {
    val rotation = if (showDoneItems) 1 else -180

    val (lastRotation, setLastRotation) = remember { mutableStateOf(0) } // this keeps last rotation
    var newRotation = lastRotation // newRotation will be updated in proper way
    val modLast = if (lastRotation > 0) lastRotation % 360 else 360 - (-lastRotation % 360) // last rotation converted to range [-359; 359]

    if (modLast != rotation) { // if modLast isn't equal rotation retrieved as function argument it means that newRotation has to be updated
        val backward = if (rotation > modLast) modLast + 360 - rotation else modLast - rotation // distance in degrees between modLast and rotation going backward
        val forward = if (rotation > modLast) rotation - modLast else 360 - modLast + rotation // distance in degrees between modLast and rotation going forward

        // update newRotation so it will change rotation in the shortest way
        newRotation = if (backward < forward) {
            lastRotation - backward // backward rotation is shorter
        } else {
            lastRotation + forward // forward rotation is shorter (or they are equal)
        }
        setLastRotation(newRotation)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val angle: Float by animateFloatAsState(
            targetValue = -newRotation.toFloat(),
            animationSpec = tween(
                durationMillis = 150,
                easing = LinearEasing
            )
        )

        Text(text = "$text ($checkedItems)", color = MaterialTheme.colorScheme.primary)
        Icon(
            modifier = Modifier.rotate(angle),
            painter = rememberVectorPainter(image = ImageVector.vectorResource(R.drawable.ic_arrow)),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListScreenPreview() {
    One4allTheme {
        ListScreen(object : IListViewModel {
            override val uiState: SnapshotStateList<QListItemType>
                get() = SnapshotStateList<QListItemType>()

            override val isCreationMode: Boolean
                get() = false
            override var showUncheckedItems: MutableState<Boolean>
                get() = mutableStateOf(true)
                set(value) {}
            override var numCheckedItems: MutableState<Int>
                get() = mutableStateOf(0)
                set(value) {}

            override fun doneClicked() {
                TODO("Not yet implemented")
            }

            override fun onCheckBoxChange(qLisItem: QListItemType.QListItemCheckBox) {
                TODO("Not yet implemented")
            }

        })
    }
}
