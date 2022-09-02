package com.unatxe.quicklist.components

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.components.search.FavouriteIconComponent
import com.unatxe.quicklist.entities.qList.QListView
import com.unatxe.quicklist.entities.qList.QListViewImpl
import com.unatxe.quicklist.helpers.DateUtils
import com.unatxe.quicklist.ui.theme.One4allTheme
import com.unatxe.quicklist.ui.theme.bodyRegular
import com.unatxe.quicklist.ui.theme.h5Bold
import org.joda.time.DateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationGraphicsApi::class)
@Composable
fun QListSummaryComponent(
    modifier: Modifier,
    qList: QListView,
    onDetailListClick: (id: Int) -> Unit = {},
    onFavoriteClick: (qListCompose: QListView) -> Unit = {}
) {
    Card(
        modifier
            .clickable {
                onDetailListClick(qList.id)
            }
            .fillMaxWidth()
            .height(IntrinsicSize.Min)

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .fillMaxHeight()
                    .width(6.dp)

            )
            Box(Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column() {
                        Text(qList.name, style = h5Bold)
                        Text(
                            DateUtils.formatCardListDate(
                                qList.updatedAt.value,
                                LocalContext.current
                            ),
                            style = bodyRegular
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                    ) {
                        FavouriteIconComponent(qList.isFavourite) {
                            onFavoriteClick(qList)
                        }
                        Text(qList.getTotalAndChecked(), style = bodyRegular)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QListSummaryComponentPreview() {
    One4allTheme {
        QListSummaryComponent(
            Modifier,
            QListViewImpl(
                id = 1,
                name = "Lista ejemplo",
                isFavourite = remember { mutableStateOf(true) },
                createdAt = DateTime(),
                updatedAt = remember {
                    mutableStateOf(DateTime())
                },
                items = remember { mutableStateListOf() }
            )
        )
    }
}
