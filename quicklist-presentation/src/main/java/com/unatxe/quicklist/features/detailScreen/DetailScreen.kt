package com.unatxe.quicklist.features.detailScreen

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility as AnimatedVisibility1
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.R
import com.unatxe.quicklist.components.search.FavouriteIconComponent
import com.unatxe.quicklist.entities.qList.QListViewImpl
import com.unatxe.quicklist.features.detailScreen.component.QListItemLazyComponent
import com.unatxe.quicklist.ui.theme.One4allTheme
import com.unatxe.quicklist.ui.theme.h3Regular
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class
)
@Composable
fun ListScreen(viewModel: DetailViewModel) {
    val uiState by viewModel.viewState.collectAsState()

    val onEvent: (event: DetailViewModelEvent) -> Unit = {
        (viewModel::onEvent)(it)
    }

    val onEventRemember = remember {
        onEvent
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                modifier = Modifier.shadow(elevation = 10.dp),
                title = {
                    Text(
                        text = uiState.name,
                        style = h3Regular
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(DetailViewModelEvent.BackClicked)
                    }) {
                        Icon(Icons.Filled.ArrowBack, "Back Previous Page")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                actions = {
                    FavouriteIconComponent(uiState.isFavourite) {
                        viewModel.onEvent(DetailViewModelEvent.FavouriteClicked)
                    }
                }

            )
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )

            ) {
                if (uiState.isCreationMode.value) {
                    Text("ListScreen without id")
                } else {
                    if (uiState.isInitialized.value) {
                        QListItemLazyComponent(
                            modifier = Modifier.weight(1f, true),
                            uiState = uiState,
                            eventEmitter = onEventRemember
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility1(
                visible = uiState.itemIsSelected.value.not(),
                enter = scaleIn(),
                exit = scaleOut()
                /*enter = slideInHorizontally(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    ),
                    initialOffsetX = {
                        it + it / 2
                    }
                    // Expand from the top.

                ),*/
                /*exit = slideOutHorizontally(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    ),
                    targetOffsetX = {
                        it + it / 2
                    }
                )*/
            ) {
                FloatingActionButton(
                    onClick = { }
                ) {
                    Icon(Icons.Filled.Add, stringResource(id = R.string.add_item))
                }
            }
        },
        bottomBar = {
            AnimatedVisibility1(
                visible = uiState.itemIsSelected.value,
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
                    modifier = Modifier.height(72.dp).imePadding(),
                    floatingActionButton = {
                        SmallFloatingActionButton(
                            onClick = {
                                viewModel.onEvent(DetailViewModelEvent.EditRequest())
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

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListScreenPreview() {
    One4allTheme {
        ListScreen(object : DetailViewModel {
            override val viewState: StateFlow<QListViewImpl>
                get() = MutableStateFlow(QListViewImpl())

            override fun onEvent(detailViewModelEvent: DetailViewModelEvent) {
            }
        })
    }
}
