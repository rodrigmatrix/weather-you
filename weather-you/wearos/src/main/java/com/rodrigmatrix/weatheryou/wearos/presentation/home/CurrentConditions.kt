package com.rodrigmatrix.weatheryou.wearos.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.wearos.presentation.components.WeatherIcon

@Composable
fun CurrentConditions(
    weatherLocation: WeatherLocation
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp)
    ) {
        WeatherIcon(
            weatherIcons = weatherLocation.weatherIcons,
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.padding(bottom = 4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = weatherLocation.currentWeather.temperatureString(),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.display3
            )
            Spacer(Modifier.padding(end = 4.dp))
            Column {
                Text(
                    text = weatherLocation.currentWeatherDescription,
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.caption1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = weatherLocation.name,
                    style = MaterialTheme.typography.caption2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(Modifier.padding(bottom = 4.dp))
        MaxAndLowestWeather(
            max = weatherLocation.maxTemperature,
            lowest = weatherLocation.lowestTemperature
        )
    }
}