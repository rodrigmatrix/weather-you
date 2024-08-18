package com.rodrigmatrix.weatheryou.wearos.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.rodrigmatrix.weatheryou.core.extensions.getHourString
import com.rodrigmatrix.weatheryou.core.extensions.getHourWithMinutesString
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.wearos.presentation.components.WeatherIcon

@Composable
fun WeatherHour(weatherHour: WeatherHour) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = weatherHour.dateTime.getHourString(context),
            style = WeatherYouTheme.typography.caption2
        )
        WeatherIcon(
            weatherIcons = weatherHour.weatherIcons,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = weatherHour.temperature.temperatureString(),
            style = WeatherYouTheme.typography.caption2
        )
    }
}