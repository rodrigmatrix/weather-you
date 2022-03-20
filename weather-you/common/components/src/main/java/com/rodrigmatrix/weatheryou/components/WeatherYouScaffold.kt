package com.rodrigmatrix.weatheryou.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherYouScaffold(
    modifier: Modifier = Modifier,
    navigationRail: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    isExpandedScreen: Boolean,
    content: @Composable () -> Unit
) {
    if (isExpandedScreen) {
        Row(modifier = modifier) {
            navigationRail()
            Column(Modifier.weight(1f)) {
                content()
            }
        }
    } else {
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar,
            modifier = modifier
        ) {
            content()
        }
    }
}