package com.unatxe.quicklist.components

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.R
import com.unatxe.quicklist.entities.QListCompose
import com.unatxe.quicklist.helpers.DateUtils
import com.unatxe.quicklist.ui.theme.One4allTheme
import org.joda.time.DateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationGraphicsApi::class)
@Composable
fun QListSummaryComponent(
    modifier: Modifier,
    qList: QListCompose,
    onDetailListClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {}
) {
    Card(
        modifier
            .clickable {
                onDetailListClick()
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
                        Text(qList.name)
                        Text(DateUtils.formatCardListDate(qList.updatedAt, LocalContext.current))
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            onFavoriteClick()
                        }
                    ) {
                        val image = AnimatedImageVector.animatedVectorResource(
                            R.drawable.heart_unchecked_to_checked
                        )

                        Icon(
                            painter = rememberAnimatedVectorPainter(image, qList.isFavourite.value),
                            contentDescription = null,
                            Modifier.size(width = 24.dp, height = 24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        val totalAndChecked = qList.getTotalAndChecked()
                        Text("${totalAndChecked.second} / ${totalAndChecked.first}")
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
            QListCompose(
                id = 1,
                name = "Lista ejemplo",
                isFavourite = remember { mutableStateOf(true) },
                createdAt = DateTime(),
                updatedAt = DateTime(),
                items = remember { mutableStateListOf() }
            )
        )
    }
}
