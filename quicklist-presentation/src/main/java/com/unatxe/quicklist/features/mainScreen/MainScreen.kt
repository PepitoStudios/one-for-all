package com.unatxe.quicklist.features.mainScreen

import ExcludeFromJacocoGeneratedReport
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.R
import com.unatxe.quicklist.components.QListSummaryComponent
import com.unatxe.quicklist.components.search.SearchComponent
import com.unatxe.quicklist.helpers.ViewModelPreviewHelper.previewMainViewModel
import com.unatxe.quicklist.ui.theme.One4allTheme
import com.unatxe.quicklist.ui.theme.h3Medium

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    viewModel: IMainViewModel
) {
    viewModel.updateList
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
            Column() {
                SearchComponent(
                    modifier = Modifier.padding(
                        start = 40.dp,
                        end = 40.dp,
                        top = padding.calculateTopPadding(),
                        bottom = 24.dp
                    ),
                    text = "",
                    label = stringResource(id = R.string.search_list),
                    onValueChange = {
                        // searchText = it
                        viewModel.searchChanged(it)
                    }
                )
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(
                        items = uiState,
                        key = { it.id }
                    ) {
                        QListSummaryComponent(
                            Modifier.animateItemPlacement(),
                            it,
                            onDetailListClick = {
                                viewModel.listClicked(it.id)
                            },
                            onFavoriteClick = {
                                viewModel.favouriteClicked(it)
                            }

                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.listClicked(null) }
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        }
    )
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {
    One4allTheme {
        MainScreen(previewMainViewModel())
    }
}
