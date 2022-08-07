package com.unatxe.quicklist.components.search

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unatxe.quicklist.R
import com.unatxe.quicklist.helpers.noRippleClickable

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun FavouriteIconComponent(isFavourite: MutableState<Boolean>, onFavoriteClick: () -> Unit) {
    val image = AnimatedImageVector.animatedVectorResource(
        R.drawable.heart_unchecked_to_checked
    )
    Icon(
        painter = rememberAnimatedVectorPainter(image, isFavourite.value),
        contentDescription = null,
        Modifier.size(width = 40.dp, height = 40.dp).padding(8.dp)
            .noRippleClickable {
                onFavoriteClick()
            },
        tint = MaterialTheme.colorScheme.primary
    )
}
