package com.rodrigmatrix.weatheryou.components

import androidx.compose.material3.Divider
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private const val DividerAlpha = 0.12f

@Composable
fun WeatherYouDivider(modifier: Modifier) {
    Divider(
        modifier,
        color = WeatherYouTheme.colorScheme.onBackground.copy(alpha = DividerAlpha)
    )
}