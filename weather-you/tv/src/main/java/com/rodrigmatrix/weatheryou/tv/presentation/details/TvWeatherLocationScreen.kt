package com.rodrigmatrix.weatheryou.tv.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.details.CurrentWeatherContent
import com.rodrigmatrix.weatheryou.components.details.FutureDaysForecastContent
import com.rodrigmatrix.weatheryou.components.details.HourlyForecastContent
import com.rodrigmatrix.weatheryou.components.details.HumidityCardContent
import com.rodrigmatrix.weatheryou.components.details.SunriseSunsetCardContent
import com.rodrigmatrix.weatheryou.components.details.UvIndexCardContent
import com.rodrigmatrix.weatheryou.components.details.VisibilityCardContent
import com.rodrigmatrix.weatheryou.components.details.WindCardContent
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.tv.components.TvCard

@Composable
fun TvWeatherLocationScreen(
    weatherLocation: WeatherLocation,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            Spacer(Modifier.height(4.dp))
        }
        item {
            WeatherInfoCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                CurrentWeatherContent(
                    weatherLocation = weatherLocation,
                )
            }
        }
        item {
            WeatherInfoCard(modifier = Modifier.padding(horizontal = 16.dp)) {
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
                            weatherLocation.unit,
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
                        VisibilityCardContent(
                            weatherLocation.visibility,
                            weatherLocation.unit,
                        )
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
                    sunrise = weatherLocation.sunrise,
                    sunset = weatherLocation.sunset,
                    currentTime = weatherLocation.currentTime,
                )
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
private fun WeatherInfoCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    TvCard(
        onClick = { },
        modifier = modifier,
        content = content,
    )
}