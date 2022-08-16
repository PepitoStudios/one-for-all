package com.unatxe.quicklist.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
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
    onListItemValueChange: (String) -> Unit,
    onFocusChange: (Boolean, MutableState<Boolean>) -> Unit,
    modifier: Modifier
) {
    val editText = rememberSaveable { mutableStateOf(text) }

    val isFocused = remember { mutableStateOf(false) }

    val editable = remember { mutableStateOf(false) }


    val background = if (isFocused.value) MaterialTheme.colorScheme.surfaceVariant else {
        MaterialTheme.colorScheme.surface
    }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Row(
        modifier
            .background(background)
            .fillMaxWidth()
            .height(56.dp)
            .clickable {
                if (isFocused.value) {
                    focusManager.clearFocus()
                } else {
                    focusRequester.requestFocus()
                }
            }
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused.value = it.isFocused
                onFocusChange.invoke(it.isFocused, editable)
            }
            .focusable()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked.value, onCheckedChange = {
            onCheckBoxCheckedChange(it)
        })
        var style = MaterialTheme.typography.bodyLarge
        if (checked.value) {
            style = style.copy(textDecoration = TextDecoration.LineThrough)
        }
        if (editable.value) {
            val textFieldFocusRequester = remember { FocusRequester() }
            var textFieldFocused = remember { false }
            BasicTextField(
                value = editText.value,
                onValueChange = {
                    editText.value = it
                    onListItemValueChange(it)
                },
                textStyle = style,
                readOnly = false,
                singleLine = true,
                modifier = Modifier.padding(16.dp)
                    .focusRequester(textFieldFocusRequester)
                    .onFocusChanged {
                        isFocused.value = it.isFocused
                        if (!isFocused.value){
                            //editable.value = false
                        }
                        textFieldFocused = it.isFocused
                    }
            )
            if (!textFieldFocused) {
                textFieldFocusRequester.requestFocus()
            }
        } else {
            Text(text = editText.value, style = style, modifier = Modifier.padding(16.dp))
        }
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
            onListItemValueChange = {},
            onFocusChange = { _, _ -> },
            modifier = Modifier
        )
    }
}
