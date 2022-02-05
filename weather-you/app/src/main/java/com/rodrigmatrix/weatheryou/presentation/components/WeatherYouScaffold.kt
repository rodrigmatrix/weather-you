package com.rodrigmatrix.weatheryou.presentation.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherYouScaffold(
    modifier: Modifier = Modifier,
    navigationRail: @Composable () -> Unit = {},
    detailContent: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isExpandedScreen: Boolean,
    detailContentVisible: Boolean,
    content: @Composable () -> Unit
) {
    val angle: Float by animateFloatAsState(
        targetValue = if (detailContentVisible) 1F else 0.1F,
        animationSpec = tween(
            durationMillis = 300,
            easing = if (detailContentVisible) LinearOutSlowInEasing else LinearOutSlowInEasing
        )
    )
    if (isExpandedScreen) {
        Row(modifier = modifier) {
            navigationRail()
            Column(Modifier.weight(1f)) {
                content()
            }
            if (detailContentVisible) {
                Column(
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .weight(angle)
                ) {
                    detailContent()
                }
            }
        }
    } else {
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            modifier = modifier
        ) {
            content()
        }
    }


}