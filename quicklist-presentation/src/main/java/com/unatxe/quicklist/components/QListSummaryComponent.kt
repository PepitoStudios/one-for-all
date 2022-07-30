package com.unatxe.quicklist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.ui.theme.One4allTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QListSummaryComponent(qList: QList, onDetailListClick: (listId: Int) -> Unit = {}) {
    Card(
        Modifier
            .clickable {
                onDetailListClick(qList.id)
            }
            .fillMaxWidth()
    ) {
        Column(
            Modifier
                .padding(all = 5.dp)
        ) {
            Column() {
                Text(qList.name)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QListSummaryComponentPreview() {
    One4allTheme {
        QListSummaryComponent(QList(1, "Lista ejemplo", true))
    }
}
