package com.unatxe.quicklist.features.detailScreen.component

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.unatxe.quicklist.R
import com.unatxe.quicklist.helpers.Animations
import com.unatxe.quicklist.ui.theme.One4allTheme

@Composable
fun QListDefaultDoneItemComponent(
    modifier: Modifier,
    text: String,
    checkedItems: State<Int>,
    showDoneItems: State<Boolean>
) {
    val rotation = remember { mutableStateOf(0) }

    rotation.value = if (showDoneItems.value) {
        Animations.processNewRotation(lastRotation = rotation.value, 1)
    } else {
        Animations.processNewRotation(lastRotation = rotation.value, -180)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val angle: Float by animateFloatAsState(
            targetValue = -rotation.value.toFloat(),
            animationSpec = tween(
                durationMillis = 150,
                easing = LinearEasing
            )
        )

        Text(text = "$text (${checkedItems.value})", color = MaterialTheme.colorScheme.primary)

        Icon(
            modifier = Modifier.rotate(angle),
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListScreenPreview() {
    One4allTheme {
        QListDefaultDoneItemComponent(
            modifier = Modifier,
            text = "Pepito",
            checkedItems = remember { mutableStateOf(2) } ,
            showDoneItems = remember { mutableStateOf(false) }

        )
    }
}
