package com.rodrigmatrix.weatheryou.presentation.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewFutureDaysForecast
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewHourlyForecast
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewWeatherLocation

@Composable
fun DetailsScreen(
    weatherLocation: WeatherLocation
) {
    LazyColumn {
        item {
            CurrentWeather(
                weatherLocation,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 10.dp,
                    bottom = 10.dp
                )
            )
        }
        item {
            HourlyForecast(
                hoursList = weatherLocation.hours,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 10.dp,
                    bottom = 10.dp
                )
            )
        }
        item {
            FutureDaysForecast(
                futureDaysList = weatherLocation.days,
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    )
            )
        }
    }
}