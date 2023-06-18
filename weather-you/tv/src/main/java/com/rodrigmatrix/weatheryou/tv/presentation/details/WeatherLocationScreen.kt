package com.rodrigmatrix.weatheryou.tv.presentation.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.ToggleableSurfaceDefaults
import com.rodrigmatrix.weatheryou.components.details.CurrentWeatherContent
import com.rodrigmatrix.weatheryou.components.details.FutureDaysForecastContent
import com.rodrigmatrix.weatheryou.components.details.HourlyForecastContent
import com.rodrigmatrix.weatheryou.components.details.HumidityCardContent
import com.rodrigmatrix.weatheryou.components.details.SunriseSunsetCardContent
import com.rodrigmatrix.weatheryou.components.details.UvIndexCardContent
import com.rodrigmatrix.weatheryou.components.details.VisibilityCardContent
import com.rodrigmatrix.weatheryou.components.details.WindCardContent
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

@Composable
fun WeatherLocationScreen(
    weatherLocation: WeatherLocation,
    modifier: Modifier = Modifier,
) {
    TvLazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier,
    ) {
        item {
            WeatherInfoCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                CurrentWeatherContent(
                    weatherLocation = weatherLocation,
                )
            }
        }
        item {
            WeatherInfoCard( modifier = Modifier.padding(horizontal = 16.dp)) {
                HourlyForecastContent(hoursList = weatherLocation.hours)
            }
        }
        item {
            WeatherInfoCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                FutureDaysForecastContent(
                    futureDaysList = weatherLocation.days,
                    isExpanded = false,
                    onExpandedButtonClick = { },
                )
            }
        }
        item {
            Row {
                Column(Modifier.weight(1f)) {
                    WeatherInfoCard(modifier = Modifier.padding(start = 16.dp, end = 8.dp)) {
                        WindCardContent(
                            weatherLocation.windSpeed,
                            weatherLocation.windDirection,
                        )
                    }

                }
                Column(Modifier.weight(1f)) {
                    WeatherInfoCard(modifier = Modifier.padding(start = 8.dp, end = 16.dp)) {
                        HumidityCardContent(
                            weatherLocation.humidity,
                            weatherLocation.dewPoint,
                        )
                    }
                }
            }
        }
        item {
            Row {
                Column(Modifier.weight(1f)) {
                    WeatherInfoCard(modifier = Modifier.padding(start = 16.dp, end = 8.dp)) {
                        VisibilityCardContent(weatherLocation.visibility)
                    }

                }
                Column(Modifier.weight(1f)) {
                    WeatherInfoCard(modifier = Modifier.padding(start = 8.dp, end = 16.dp)) {
                        UvIndexCardContent(weatherLocation.uvIndex)
                    }
                }
            }
        }
        item {
            WeatherInfoCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                SunriseSunsetCardContent(
                    sunriseHour = weatherLocation.sunrise,
                    sunsetHour = weatherLocation.sunset,
                    currentTime = weatherLocation.currentTime,
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun WeatherInfoCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val cardShape = CircleShape.copy(CornerSize(20.dp))
    Surface(
        onClick = { },
        color = ClickableSurfaceDefaults.color(
            color = MaterialTheme.colorScheme.primaryContainer,
            focusedColor = MaterialTheme.colorScheme.primaryContainer,
            pressedColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                shape = cardShape,
            )
        ),
        scale = ClickableSurfaceDefaults.scale(
            scale = 1f,
            focusedScale = 1f,
        ),
        modifier = modifier,
    ) {
        content()
    }
}