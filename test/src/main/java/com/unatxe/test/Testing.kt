package com.unatxe.test
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.unatxe.test.ui.theme.One4allTheme

class Testing {

    @Composable
    fun ContainerTest(defaultValue: String) {
        var text by rememberSaveable { mutableStateOf("") }

        Test(text) { textChanged ->
            text = textChanged
        }
    }

    @Composable
    fun Test(text: String, onValueChange: (String) -> Unit) {
        TextField(
            value = text,
            onValueChange = onValueChange,
            label = { Text("Label") },
            singleLine = true
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun TestContainerPreview() {
        ContainerTest(defaultValue = "hola que haze")
    }

    @Preview(showBackground = true)
    @Composable
    fun TestContainerWithThemePreview() {
        One4allTheme(dynamicColor = false) {
            ContainerTest(defaultValue = "hola que haze")
        }
    }
}
