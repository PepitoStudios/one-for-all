package com.unatxe.quicklist.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.entities.qList.item.QListItemViewCheckBoxImpl

import com.unatxe.quicklist.features.detailScreen.DetailViewModelEvent
import com.unatxe.quicklist.features.detailScreen.component.QListDetailEditItemComponent
import com.unatxe.quicklist.ui.theme.One4allTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QListDetailCheckItemComponent(
    model: QListItemViewCheckBoxImpl,
    eventEmitter: (event: DetailViewModelEvent) -> Unit,
    modifier: Modifier
) {
    val checkedChanged: (Boolean) -> Unit = {
        eventEmitter(DetailViewModelEvent.CheckBoxChange(model))
    }
    Row(
        modifier
            .background(
                if (model.isFocused.value) {
                    MaterialTheme.colorScheme.surfaceVariant
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
            .fillMaxWidth()
            .height(56.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        eventEmitter(DetailViewModelEvent.EditRequest(model))
                    },
                    onTap = { eventEmitter(DetailViewModelEvent.FocusRequest(model)) }
                )
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = model.checked.value,
            enabled = model.isCheckBoxEnabled.value,
            onCheckedChange = remember { checkedChanged }
        )
        var style = MaterialTheme.typography.bodyLarge
        if (model.checked.value) {
            style = style.copy(textDecoration = TextDecoration.LineThrough)
        }
        if (model.isEditMode.value) {
            QListDetailEditItemComponent(model, eventEmitter)
        } else {
            Text(text = model.text.value, style = style, modifier = Modifier.padding(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QListDetailCheckItemComponentPreview() {
    One4allTheme {
        QListDetailCheckItemComponent(
            model = QListItemViewCheckBoxImpl(
                id = 1,
                text = remember { mutableStateOf("Test") },
                checked = remember { mutableStateOf(false) },
                isEditMode = remember { mutableStateOf(false) },
                idList = 1
            ),
            eventEmitter = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QListDetailCheckedItemComponentPreview() {
    One4allTheme {
        QListDetailCheckItemComponent(
            model = QListItemViewCheckBoxImpl(
                id = 1,
                text = remember { mutableStateOf("Test") },
                checked = remember { mutableStateOf(true) },
                isEditMode = remember { mutableStateOf(false) },
                idList = 1
            ),
            eventEmitter = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QListDetailCheckedEditmodePreview() {
    One4allTheme {
        QListDetailCheckItemComponent(
            model = QListItemViewCheckBoxImpl(
                id = 1,
                text = remember { mutableStateOf("Test") },
                checked = remember { mutableStateOf(true) },
                isEditMode = remember { mutableStateOf(true) },
                idList = 1
            ),
            eventEmitter = {},
            modifier = Modifier
        )
    }
}
