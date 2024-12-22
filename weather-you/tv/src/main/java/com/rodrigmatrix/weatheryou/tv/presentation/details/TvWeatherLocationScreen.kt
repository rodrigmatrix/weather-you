package com.rodrigmatrix.weatheryou.tv.presentation.details

import android.annotation.SuppressLint
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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
import com.rodrigmatrix.weatheryou.components.particle.WeatherAnimationsBackground
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.core.extensions.getDateTimeFromTimezone
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.tv.components.TvCard
import com.rodrigmatrix.weatheryou.tv.presentation.theme.md_theme_dark_secondaryContainer

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TvWeatherDetailsScreen(
    weatherLocation: WeatherLocation,
    onExpandDay: (WeatherDay) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        containerColor = if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
            Color.Transparent
        } else {
            WeatherYouTheme.colorScheme.background
        },
        modifier = modifier,
    ) { paddingValues ->
        if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
            WeatherAnimationsBackground(
                weatherLocation = weatherLocation,
            )
        }
        TvWeatherLocationScreen(
            weatherLocation = weatherLocation,
            onExpandDay = onExpandDay,
        )
    }
}

@Composable
fun TvWeatherLocationScreen(
    weatherLocation: WeatherLocation,
    onExpandDay: (WeatherDay) -> Unit,
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
                            colors = CardDefaults.colors(
                                containerColor =  if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
                                    Color.Transparent
                                } else {
                                    WeatherYouTheme.colorScheme.secondaryContainer
                                },
                            ),
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
                    minWeekTemperature = weatherLocation.minWeekTemperature,
                    maxWeekTemperature = weatherLocation.maxWeekTemperature,
                    currentTemperature = weatherLocation.currentWeather,
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
                                    focusedContainerColor = WeatherYouTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                    containerColor = if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
                                        md_theme_dark_secondaryContainer.copy(alpha = 0.4f)
                                    } else {
                                        WeatherYouTheme.colorScheme.secondaryContainer
                                    },
                                ),
                                glow = ClickableSurfaceDefaults.glow(
                                    focusedGlow = Glow(
                                        elevationColor = if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
                                            md_theme_dark_secondaryContainer.copy(alpha = 0.4f)
                                        } else {
                                            WeatherYouTheme.colorScheme.tertiary
                                        },
                                        elevation = 3.dp,
                                    )
                                ),
                                shape = ClickableSurfaceDefaults.shape(shape = RectangleShape),
                            ) {
                                DayContent(
                                    day = day,
                                    minWeekTemperature = weatherLocation.minWeekTemperature,
                                    maxWeekTemperature = weatherLocation.maxWeekTemperature,
                                    currentTemperature = day.temperature,
                                    index = index,
                                )
                            }
                            ExpandedCardContent(
                                day = day,
                                isExpanded = isDayExpanded,
                                hourItemWrapper = { content ->
                                    TvCard(
                                        onClick = { },
                                        colors = CardDefaults.colors(
                                            containerColor =  if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
                                                Color.Transparent
                                            } else {
                                                WeatherYouTheme.colorScheme.secondaryContainer
                                            },
                                        ),
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
                    },
                    onExpandDay = onExpandDay,
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
                    currentTime = weatherLocation.timeZone.getDateTimeFromTimezone(),
                    isDaylight = weatherLocation.isDaylight,
                )
            }
            Spacer(Modifier.height(16.dp))
        }
        item {
            AppleWeatherAttribution()
        }
    }
}

@Composable
fun AppleWeatherAttribution(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(
                when {
                    WeatherYouTheme.themeSettings.showWeatherAnimations -> com.rodrigmatrix.weatheryou.locationdetails.R.drawable.ic_apple_weather_light
                    WeatherYouTheme.isDarkTheme -> com.rodrigmatrix.weatheryou.locationdetails.R.drawable.ic_apple_weather_light
                    else -> com.rodrigmatrix.weatheryou.locationdetails.R.drawable.ic_apple_weather_dark
                }
            ),
            contentDescription = "Apple Weather",
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = buildAnnotatedString {
                append("For more information, ")
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline
                    ),
                ) {
                    append(
                        "visit Apple Weather",
                    )
                }
            },
            style = WeatherYouTheme.typography.bodyMedium,
            color = WeatherYouTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable(onClick = {
                    val intent = CustomTabsIntent.Builder()
                        .build()
                    intent.launchUrl(context, Uri.parse("https://developer.apple.com/weatherkit/data-source-attribution/"))
                })
        )
        Spacer(Modifier.height(16.dp))
    }
}