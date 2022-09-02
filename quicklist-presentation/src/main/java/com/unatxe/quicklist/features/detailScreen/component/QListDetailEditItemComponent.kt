package com.unatxe.quicklist.features.detailScreen.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.components.QListDetailCheckItemComponent
import com.unatxe.quicklist.entities.QList.item.QListItemViewCheckBox
import com.unatxe.quicklist.entities.qList.item.QListItemViewCheckBoxImpl
import com.unatxe.quicklist.features.detailScreen.DetailViewModelEvent
import com.unatxe.quicklist.ui.theme.One4allTheme

@Composable
fun QListDetailEditItemComponent(
    itemCheckBox: QListItemViewCheckBox,
    eventEmitter: (event: DetailViewModelEvent) -> Unit
) {
    val textFieldFocusRequester = remember { FocusRequester() }

    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = itemCheckBox.text.value,
                selection = TextRange(
                    itemCheckBox.text.value.length
                )
            )
        )
    }
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp) // .background(
        // MaterialTheme.colorScheme.surfaceVariant
        /*)*/,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        BasicTextField(
            value = textFieldValueState,
            onValueChange = {
                eventEmitter.invoke(
                    DetailViewModelEvent.ListItemValueChange(itemCheckBox, textFieldValueState.text)
                )
                textFieldValueState = it
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Left),
            singleLine = true,
            readOnly = false,
            modifier = Modifier.fillMaxWidth()
                .focusRequester(textFieldFocusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    eventEmitter.invoke(DetailViewModelEvent.EditRequest())
                }
            )
        )
        LaunchedEffect(Unit) {
            textFieldFocusRequester.requestFocus()
        }
    }
}


@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QListDetailEditItemPreview() {
    One4allTheme {
        QListDetailEditItemComponent(
            itemCheckBox = QListItemViewCheckBoxImpl(
                id = 1,
                text = remember { mutableStateOf("hola") },
                checked = remember { mutableStateOf(true) },
                idList = 1
            ),
            eventEmitter = {}
        )
    }
}
