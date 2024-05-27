package com.rodrigmatrix.weatheryou.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WeatherYouCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    color: Color = MaterialTheme.colorScheme.secondaryContainer,
    content: @Composable () -> Unit
) {
    Surface(
        color = color,
        shape = shape,
        modifier = modifier
            .fillMaxWidth(),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun WeatherYouCard(
    modifier: Modifier = Modifier,
    isDismissible: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    color: Color = MaterialTheme.colorScheme.secondaryContainer,
    content: @Composable () -> Unit
) {
    if (isDismissible) {
        val dismissState = rememberSwipeToDismissBoxState(
            confirmValueChange = {
                if (it != SwipeToDismissBoxValue.Settled) {
                    onDismiss()
                    true
                } else {
                    false
                }
            },
        )
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                SwipeBackground(
                    shape = shape,
                    dismissState = dismissState,
                )
            },
            content = {
                Surface(
                    color = color,
                    shape = shape,
                    onClick = onClick,
                    modifier = modifier
                        .fillMaxWidth(),
                    content = content
                )
            }
        )
    } else {
        Surface(
            color = color,
            shape = shape,
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth(),
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBackground(
    shape: RoundedCornerShape,
    dismissState: SwipeToDismissBoxState
) {
    val scale by animateFloatAsState(
        if (dismissState.targetValue == SwipeToDismissBoxValue.StartToEnd) 0.75f else 1f,
        label = ""
    )

    Surface(
        shape = shape,
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.fillMaxSize(),
        ) {
            Icon(
                Icons.Default.Delete,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null,
                modifier = Modifier
                    .scale(scale)
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
        }
    }
}
