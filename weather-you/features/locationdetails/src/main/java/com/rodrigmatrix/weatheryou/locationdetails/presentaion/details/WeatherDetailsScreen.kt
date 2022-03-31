package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.locationdetails.R
import com.rodrigmatrix.weatheryou.components.WeatherYouLargeAppBar
import com.rodrigmatrix.weatheryou.components.WeatherYouSmallAppBar
import com.rodrigmatrix.weatheryou.components.extensions.dpadFocusable
import com.rodrigmatrix.weatheryou.core.extensions.getLocalTime
import com.rodrigmatrix.weatheryou.core.extensions.getTimeZoneCurrentTime
import com.rodrigmatrix.weatheryou.core.extensions.getTimeZoneTimestamp
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.preview.PreviewFutureDaysForecast
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.preview.PreviewHourlyForecast
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.preview.PreviewWeatherLocation
import org.koin.androidx.compose.getViewModel

@Composable
fun WeatherDetailsScreen(
    weatherLocation: WeatherLocation?,
    onCloseClick: () -> Unit,
    expandedScreen: Boolean,
    onDeleteLocation: () -> Unit,
    viewModel: WeatherDetailsViewModel = getViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    viewModel.setWeatherLocation(weatherLocation)

    WeatherDetailsScreen(
        viewState = viewState,
        expandedScreen = expandedScreen,
        onExpandedButtonClick = {
            viewModel.onFutureWeatherButtonClick(it)
        },
        onCloseClick = onCloseClick,
        onDeleteClick = onDeleteLocation
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailsScreen(
    viewState: WeatherDetailsViewState,
    expandedScreen: Boolean,
    onExpandedButtonClick: (Boolean) -> Unit,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            if (expandedScreen) {
                ExpandedTopAppBar(
                    viewState.weatherLocation?.name.orEmpty(),
                    onCloseClick,
                    onDeleteClick
                )
            } else {
                SmallScreenTopAppBar(
                    viewState.weatherLocation?.name.orEmpty(),
                    onCloseClick,
                    onDeleteClick
                )
            }
        }
    ) {
        val scrollState = rememberLazyListState()
        scrollState.layoutInfo.reverseLayout
        LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp), state = scrollState) {
            item {
                viewState.weatherLocation?.let {
                    CurrentWeather(
                        it,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .dpadFocusable(0, scrollState)
                    )
                }
            }
            item {
                HourlyForecast(
                    hoursList = viewState.todayWeatherHoursList,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .dpadFocusable(1, scrollState)
                )
            }
            item {
                FutureDaysForecast(
                    futureDaysList = viewState.futureDaysList,
                    isExpanded = viewState.isFutureWeatherExpanded,
                    onExpandedButtonClick = onExpandedButtonClick,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .dpadFocusable(2, scrollState)
                )
            }
            item {
                Row {
                    Column(Modifier.weight(1f)) {
                        WindCard(
                            viewState.weatherLocation?.windSpeed ?: 0.0,
                            viewState.weatherLocation?.windDirection ?: 0.0,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 8.dp)
                                .dpadFocusable(3, scrollState)

                        )
                    }
                    Column(Modifier.weight(1f)) {
                        HumidityCard(
                            viewState.weatherLocation?.humidity ?: 0.0,
                            viewState.weatherLocation?.dewPoint ?: 0.0,
                            modifier = Modifier
                                .padding(start = 8.dp, end = 16.dp)
                                .dpadFocusable(3, scrollState)
                        )
                    }
                }
            }
            item {
                Row {
                    Column(Modifier.weight(1f)) {
                        VisibilityCard(
                            viewState.weatherLocation?.visibility ?: 0.0,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 8.dp)
                                .dpadFocusable(4, scrollState)
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        UvIndexCard(
                            viewState.weatherLocation?.uvIndex ?: 0.0,
                            modifier = Modifier
                                .padding(start = 8.dp, end = 16.dp)
                                .dpadFocusable(4, scrollState)
                        )
                    }
                }
            }
            item {
                viewState.weatherLocation?.let { weatherLocation ->
                    SunriseSunsetCard(
                        sunriseHour = weatherLocation.sunrise,
                        sunsetHour = weatherLocation.sunset,
                        currentTime = weatherLocation.timeZone.getTimeZoneTimestamp(),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .dpadFocusable(5, scrollState)
                    )
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun SmallScreenTopAppBar(
    title: String,
    onCloseClick: () -> Unit,
    onDeleteButtonClick: () -> Unit
) {
    WeatherYouSmallAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            IconButton(onClick = onDeleteButtonClick) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 16.dp)
                        .height(24.dp),
                    contentDescription = stringResource(R.string.delete_location)
                )
            }
        }
    )
}

@Composable
fun ExpandedTopAppBar(
    title: String,
    onCloseClick: () -> Unit,
    onDeleteButtonClick: () -> Unit
) {
    WeatherYouLargeAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            IconButton(onClick = onDeleteButtonClick) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 16.dp)
                        .height(24.dp),
                    contentDescription = stringResource(R.string.delete_location)
                )
            }
        },
        modifier = Modifier.animateContentSize()
    )
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun WeatherDetailsScreenPreview() {
    MaterialTheme {
        WeatherDetailsScreen(
            viewState = WeatherDetailsViewState(
                weatherLocation = PreviewWeatherLocation,
                todayWeatherHoursList = PreviewHourlyForecast,
                futureDaysList = PreviewFutureDaysForecast
            ),
            expandedScreen = false,
            onExpandedButtonClick = { },
            onCloseClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(device = Devices.PIXEL_C)
@Preview(device = Devices.PIXEL_C, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun WeatherDetailsScreenTabletPreview() {
    MaterialTheme {
        WeatherDetailsScreen(
            viewState = WeatherDetailsViewState(
                weatherLocation = PreviewWeatherLocation,
                todayWeatherHoursList = PreviewHourlyForecast,
                futureDaysList = PreviewFutureDaysForecast
            ),
            expandedScreen = true,
            onExpandedButtonClick = { },
            onCloseClick = {},
            onDeleteClick = {}
        )
    }
}