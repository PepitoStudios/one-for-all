package com.unatxe.quicklist.features.mainScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unatxe.quicklist.ui.theme.One4allTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val helloWorld = viewModel.hello.observeAsState("")
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("QuickList") },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Localized description"
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors()

            )
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = helloWorld.value)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    One4allTheme {
        MainScreen()
    }
}
