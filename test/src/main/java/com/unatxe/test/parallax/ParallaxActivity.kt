package com.unatxe.test.parallax

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.unatxe.test.parallax.ui.theme.One4allTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ParallaxActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            One4allTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ParallaxScreen()
                }
            }
        }
    }
}

@Composable
fun ParallaxScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var data by remember { mutableStateOf<SensorData?>(null) }

    DisposableEffect(Unit) {
        val dataManager = SensorDataManager(context)
        dataManager.init()

        val job = scope.launch {
            dataManager.data
                .receiveAsFlow()
                .onEach { data = it }
                .collect()
        }

        onDispose {
            dataManager.cancel()
            job.cancel()
        }
    }

    Box(modifier = modifier) {
        ParallaxView(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            depthMultiplier = 20,
            data = data
        )
    }
}
