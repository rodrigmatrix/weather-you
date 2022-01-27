package com.rodrigmatrix.weatheryou.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.R

@Composable
fun ExpandButton(
    isExpanded: Boolean,
    contentDescription: String,
    onExpandButtonClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val angle: Float by animateFloatAsState(
        targetValue = if (isExpanded) 180F else 0F,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    )
    IconButton(
        onClick = {
            onExpandButtonClick(isExpanded.not())
        },
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
            .size(34.dp)

    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_down),
            contentDescription = contentDescription,
            modifier = Modifier.rotate(angle)
        )
    }
}