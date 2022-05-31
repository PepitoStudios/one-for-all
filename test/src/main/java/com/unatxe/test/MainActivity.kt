package com.unatxe.test

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.unatxe.test.parallax.ParallaxActivity
import com.unatxe.test.ui.theme.One4allTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            One4allTheme(dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Section("Paralax") {
                        startActivity(Intent(this, ParallaxActivity::class.java))
                    }
                }
            }
        }
    }
}

@Composable
fun Section(name: String, onClick: () -> Unit = {}) {
    Column {
        Button(onClick = onClick) {
            Text(text = name)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    One4allTheme {
        Section("Android")
    }
}
