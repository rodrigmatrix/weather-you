package com.rodrigmatrix.weatheryou.wearos.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun WeatherYouTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = WearColorPalette,
        typography = Typography,
        content = content
    )
}