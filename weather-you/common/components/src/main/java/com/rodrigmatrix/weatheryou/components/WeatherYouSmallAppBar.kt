package com.rodrigmatrix.weatheryou.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherYouSmallAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Box(modifier = modifier) {
        TopAppBar(
            modifier = modifier,
            actions = actions,
            title = title,
            navigationIcon = navigationIcon
        )
    }
}