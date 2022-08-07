package com.unatxe.quicklist.features.listScreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.tooling.preview.Preview
import com.unatxe.quicklist.entities.QListCompose
import com.unatxe.quicklist.ui.theme.One4allTheme
import org.joda.time.DateTime

@Composable
fun ListScreen(viewModel: IListViewModel) {
    val uiState = remember {
        viewModel.uiState
    }

    if (viewModel.isCreationMode) {
        Text("ListScreen without id")
    } else {
        if (uiState.value != null) {
            Text("ListScreen with id ${uiState.value?.id}")
        } else {
            Text("Loading")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    One4allTheme {
        ListScreen(object : IListViewModel {
            override val uiState: MutableState<QListCompose?>
                get() = mutableStateOf(
                    QListCompose(
                        id = 1,
                        name = "List 1",
                        isFavourite = mutableStateOf(false),
                        items = SnapshotStateList(),
                        createdAt = DateTime(),
                        updatedAt = mutableStateOf(DateTime())
                    )
                )
            override val isCreationMode: Boolean
                get() = false
        })
    }
}
