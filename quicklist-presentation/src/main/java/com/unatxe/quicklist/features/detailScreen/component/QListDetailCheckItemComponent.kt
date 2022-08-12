package com.unatxe.quicklist.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.ui.theme.One4allTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QListDetailCheckItemComponent(
    text: String,
    checked: MutableState<Boolean>,
    onCheckBoxCheckedChange: (Boolean) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(56.dp)
            .toggleable(
                value = checked.value,
                onValueChange = {
                    onCheckBoxCheckedChange(it)
                },
                role = Role.Checkbox
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked.value, onCheckedChange = null)
        var style = MaterialTheme.typography.bodyLarge
        if (checked.value) {
            style = style.copy(textDecoration = TextDecoration.LineThrough)
        }
        Text(
            text = text,
            style = style,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QListDetailCheckItemComponentPreview() {
    One4allTheme {
        QListDetailCheckItemComponent(
            text = "Checkbox",
            checked = remember { mutableStateOf(true) },
            onCheckBoxCheckedChange = {},
            modifier = Modifier
        )
    }
}