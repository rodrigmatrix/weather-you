package com.rodrigmatrix.weatheryou.tv.presentation.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Glow
import androidx.tv.material3.Surface
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.WeatherYouDivider
import com.rodrigmatrix.weatheryou.components.details.CurrentWeatherContent
import com.rodrigmatrix.weatheryou.components.details.DayContent
import com.rodrigmatrix.weatheryou.components.details.ExpandedCardContent
import com.rodrigmatrix.weatheryou.components.details.FutureDaysForecastContent
import com.rodrigmatrix.weatheryou.components.details.HourlyForecastContent
import com.rodrigmatrix.weatheryou.components.details.HumidityCardContent
import com.rodrigmatrix.weatheryou.components.details.SunriseSunsetCardContent
import com.rodrigmatrix.weatheryou.components.details.UvIndexCardContent
import com.rodrigmatrix.weatheryou.components.details.VisibilityCardContent
import com.rodrigmatrix.weatheryou.components.details.WindCardContent
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.tv.components.TvCard

@Composable
fun TvWeatherLocationScreen(
    weatherLocation: WeatherLocation,
    modifier: Modifier = Modifier,
) {
    var is15DaysVisible by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Spacer(Modifier.height(4.dp))
        }
        item {
            TvCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                CurrentWeatherContent(
                    weatherLocation = weatherLocation,
                )
            }
        }
        item {
            TvCard(
                modifier = Modifier
                    .focusable(enabled = false)
                    .focusProperties {
                        canFocus = false
                    }
                    .clickable(enabled = false, onClick = { })
                    .padding(horizontal = 16.dp)
            ) {
                HourlyForecastContent(
                    hoursList = weatherLocation.hours,
                    hourItemWrapper = { content ->
                        TvCard(
                            onClick = { },
                            scale = CardDefaults.scale(
                                focusedScale = 1.005f,
                            ),
                        ) {
                            content()
                        }
                    }
                )
            }
        }
        item {
            TvCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .focusable(enabled = false)
                    .focusProperties {
                        canFocus = false
                    }
                    .clickable(enabled = false, onClick = { })
            ) {
                FutureDaysForecastContent(
                    futureDaysList = if (is15DaysVisible) {
                        weatherLocation.days
                    } else {
                        weatherLocation.days.take(7)
                    },
                    isExpanded = is15DaysVisible,
                    onExpandedButtonClick = { is15DaysVisible = !is15DaysVisible },
                    dayItem = { index, day ->
                        var isDayExpanded by remember { mutableStateOf(false) }
                        Column {
                            WeatherYouDivider(Modifier.height(1.dp))
                            Surface(
                                onClick = {
                                    isDayExpanded = !isDayExpanded
                                },
                                scale = ClickableSurfaceDefaults.scale(
                                    focusedScale = 1.005f,
                                ),
                                colors = ClickableSurfaceDefaults.colors(
                                    focusedContainerColor = WeatherYouTheme.colorScheme.primaryContainer,
                                    containerColor = WeatherYouTheme.colorScheme.secondaryContainer,
                                ),
                                glow = ClickableSurfaceDefaults.glow(
                                    focusedGlow = Glow(
                                        elevationColor = WeatherYouTheme.colorScheme.tertiary,
                                        elevation = 3.dp,
                                    )
                                ),
                                shape = ClickableSurfaceDefaults.shape(shape = RectangleShape),
                            ) {
                                DayContent(
                                    day = day,
                                    index = index,
                                )
                            }
                            ExpandedCardContent(
                                day = day,
                                isExpanded = isDayExpanded,
                                hourItemWrapper = { content ->
                                    TvCard(
                                        onClick = { },
                                        scale = CardDefaults.scale(
                                            focusedScale = 1.005f,
                                        ),
                                    ) {
                                        content()
                                    }
                                }
                            )
                        }
                    },
                    expandButton = {
                        TvExpandButton(
                            isExpanded = is15DaysVisible,
                            contentDescription = stringResource(R.string.show_all_days_forecast),
                            onExpandButtonClick = {
                                is15DaysVisible = !is15DaysVisible
                            },
                        )
                    }
                )
            }
        }
        item {
            Row {
                Column(Modifier.weight(1f)) {
                    TvCard(modifier = Modifier.padding(start = 16.dp, end = 8.dp)) {
                        WindCardContent(
                            weatherLocation.windSpeed,
                            weatherLocation.windDirection,
                            weatherLocation.unit,
                        )
                    }

                }
                Column(Modifier.weight(1f)) {
                    TvCard(modifier = Modifier.padding(start = 8.dp, end = 16.dp)) {
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
                    TvCard(modifier = Modifier.padding(start = 16.dp, end = 8.dp)) {
                        VisibilityCardContent(
                            weatherLocation.visibility,
                            weatherLocation.unit,
                        )
                    }
                }
                Column(Modifier.weight(1f)) {
                    TvCard(modifier = Modifier.padding(start = 8.dp, end = 16.dp)) {
                        UvIndexCardContent(weatherLocation.uvIndex)
                    }
                }
            }
        }
        item {
            TvCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                SunriseSunsetCardContent(
                    sunrise = weatherLocation.sunrise,
                    sunset = weatherLocation.sunset,
                    currentTime = weatherLocation.currentTime,
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}