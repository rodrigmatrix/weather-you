package com.rodrigmatrix.weatheryou.presentation.components

import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private const val DividerAlpha = 0.12f

@Composable
fun WeatherYouDivider(modifier: Modifier) {
    Divider(
        modifier,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = DividerAlpha)
    )
}