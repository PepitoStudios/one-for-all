package com.unatxe.quicklist.components.search

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.unatxe.quicklist.ui.theme.One4allTheme
import com.unatxe.quicklist.ui.theme.bodyLarge

@Composable
@ExperimentalMaterial3Api
fun SearchComponent(
    modifier: Modifier,
    text: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf(text) }

    OutlinedTextField(
        value = searchText,
        onValueChange = {
            searchText = it
            onValueChange(it)
        },
        textStyle = bodyLarge,
        label = { Text(text = label) },
        modifier = modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = {
                    searchText = ""
                    onValueChange(searchText)
                }) {
                    val visibilityIcon = Icons.Outlined.Close
                    // Please provide localized description for accessibility services
                    val description = "Reset search"
                    if (searchText.isNotEmpty()) {
                        Icon(imageVector = visibilityIcon, contentDescription = description)
                    }
                }
            }
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchComponentPreview() {
    One4allTheme {
        SearchComponent(Modifier, "Hola que hace", "Label") {}
    }
}
