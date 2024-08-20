package com.rodrigmatrix.weatheryou.settings.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun SettingWithOption(
    title: String,
    selected: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = title,
            color = WeatherYouTheme.colorScheme.onBackground,
            style = WeatherYouTheme.typography.titleMedium
        )
        Text(
            text = selected,
            style = WeatherYouTheme.typography.titleMedium,
            color = WeatherYouTheme.colorScheme.onBackground,
            modifier = Modifier.alpha(0.7f)
        )
    }
}