package com.unatxe.quicklist.components

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QListCheckBox(
    text: String,
    checked: Boolean,
    onCheckBoxCheckedChange: (Boolean) -> Unit,
    modifier: Modifier
) {
    val (isChecked, setChecked) =
        remember { mutableStateOf(checked) }

    Row(
        modifier
            .fillMaxWidth()
            .height(56.dp)
            .toggleable(
                value = isChecked,
                onValueChange = {
                    setChecked(it)
                    onCheckBoxCheckedChange(it)
                },
                role = Role.Checkbox
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = isChecked, onCheckedChange = null)
        var style = MaterialTheme.typography.bodyLarge
        if (checked){
            style = style.copy(textDecoration = TextDecoration.LineThrough)
        }
        Text(
            text = text,
            style = style,
            modifier = Modifier.padding(start = 16.dp),
        )
    }
}
