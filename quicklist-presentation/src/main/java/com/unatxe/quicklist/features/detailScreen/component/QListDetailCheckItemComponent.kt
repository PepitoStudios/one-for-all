package com.unatxe.quicklist.components

import android.content.res.Configuration
import android.util.Log
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
import com.unatxe.quicklist.entities.QListItemType
import com.unatxe.quicklist.ui.theme.One4allTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QListDetailCheckItemComponent(
    model: QListItemType.QListItemCheckBox,
    onCheckBoxCheckedChange: (Boolean) -> Unit,
    onListItemValueChange: (String) -> Unit,
    onFocusMode: (QListItemType.QListItemCheckBox) -> Unit,
    modifier: Modifier
) {
    val editText = rememberSaveable { model.text }

    val background = if (model.isFocused.value) MaterialTheme.colorScheme.surfaceVariant else {
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
                if (model.isFocused.value) {
                    focusManager.clearFocus()
                } else {
                    focusRequester.requestFocus()
                }
            }
            .focusRequester(focusRequester)
            .onFocusChanged {
                Log.d("Test", "onFocusChanged $it ${editText.value}")
                model.isFocused.value = it.isFocused
                onFocusMode.invoke(model)
            }
            .focusable()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = model.checked.value, onCheckedChange = {
            onCheckBoxCheckedChange(it)
        })
        var style = MaterialTheme.typography.bodyLarge
        if (model.checked.value) {
            style = style.copy(textDecoration = TextDecoration.LineThrough)
        }
        if (model.isEditMode.value) {
            Log.d("Test", "Edit Mode ${editText.value}")
            val textFieldFocusRequester = remember { FocusRequester() }
            var textFieldFocused = remember { false }
            BasicTextField(
                value = editText.value,
                onValueChange = {
                    editText.value = it
                    onListItemValueChange(it)
                },
                textStyle = style,
                singleLine = true,
                readOnly = false,
                modifier = Modifier.padding(16.dp)
                    .focusRequester(textFieldFocusRequester)
                    .onFocusChanged {
                        model.isFocused.value = it.isFocused
                        textFieldFocused = it.isFocused

                        if (it.isFocused) {
                            Log.d("Test", "Edit mode to True")
                            model.isEditModePassed = true
                        }

                        Log.d(
                            "Test",
                            "Editable TextField Focus State $it ${editText.value} firstTimeEditable -> ${model.isFirstTimeEditable} editModePassed -> ${model.isEditModePassed}"
                        )

                        if (!it.isFocused && model.isEditModePassed) {
                            Log.d("Test", "Editable TextField Focus, set editable to False")
                            model.isEditMode.value = false
                            model.isEditModePassed = false
                        }
                    }
            )
            if (!model.isEditModePassed && !textFieldFocused) {
                Log.d("Test", "Is first time Editable ${editText.value}")
                textFieldFocusRequester.requestFocus()
            }
        } else {
            Log.d("Test", "Read Only Mode ${editText.value}")
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
            model = QListItemType.QListItemCheckBox(
                id = 1,
                text = remember { mutableStateOf("Test") },
                checked = remember { mutableStateOf(false) },
                isEditMode = remember { mutableStateOf(false) },
                isEditModePassed = false,
                isFirstTimeEditable = true,
                idList = 1
            ),
            onCheckBoxCheckedChange = {},
            onListItemValueChange = {},
            onFocusMode = { },
            modifier = Modifier
        )
    }
}
