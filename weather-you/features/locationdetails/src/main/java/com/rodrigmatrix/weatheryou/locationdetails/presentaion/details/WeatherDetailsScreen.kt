package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.WeatherYouLargeAppBar
import com.rodrigmatrix.weatheryou.components.WeatherYouSmallAppBar
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.details.FutureDaysForecast
import com.rodrigmatrix.weatheryou.components.preview.PreviewFutureDaysForecast
import com.rodrigmatrix.weatheryou.components.preview.PreviewHourlyForecast
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherLocation
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import org.koin.androidx.compose.getViewModel

@Composable
fun WeatherDetailsScreen(
    weatherLocation: WeatherLocation?,
    onCloseClick: () -> Unit,
    isFullScreen: Boolean,
    onDeleteLocationClicked: () -> Unit,
    viewModel: WeatherDetailsViewModel = getViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()
    viewModel.setWeatherLocation(weatherLocation)

    WeatherDetailsScreen(
        viewState = viewState,
        isFullScreen = isFullScreen,
        onExpandedButtonClick = {
            viewModel.onFutureWeatherButtonClick(it)
        },
        onCloseClick = onCloseClick,
        onDeleteClick = onDeleteLocationClicked
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WeatherDetailsScreen(
    viewState: WeatherDetailsViewState,
    isFullScreen: Boolean,
    onExpandedButtonClick: (Boolean) -> Unit,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            if (isFullScreen) {
                ExpandedTopAppBar(
                    viewState.weatherLocation?.name.orEmpty(),
                    onCloseClick,
                    onDeleteClick,
                    viewState.weatherLocation?.isCurrentLocation?.not() == true
                )
            } else {
                SmallScreenTopAppBar(
                    viewState.weatherLocation?.name.orEmpty(),
                    onCloseClick,
                    onDeleteClick,
                    viewState.weatherLocation?.isCurrentLocation?.not() == true
                )
            }
        },
        containerColor = WeatherYouTheme.colorScheme.background,
    ) { paddingValues ->
        val scrollState = rememberLazyListState()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            state = scrollState,
            contentPadding = paddingValues,
        ) {
            item {
                viewState.weatherLocation?.let {
                    CurrentWeather(
                        it,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
            item {
                HourlyForecast(
                    hoursList = viewState.todayWeatherHoursList,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            item {
                FutureDaysForecast(
                    futureDaysList = viewState.futureDaysList,
                    isExpanded = viewState.isFutureWeatherExpanded,
                    onExpandedButtonClick = onExpandedButtonClick,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            item {
                Row {
                    Column(Modifier.weight(1f)) {
                        WindCard(
                            viewState.weatherLocation?.windSpeed ?: 0.0,
                            viewState.weatherLocation?.windDirection ?: 0.0,
                            unit = viewState.weatherLocation?.unit ?: TemperaturePreference.METRIC,
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        HumidityCard(
                            viewState.weatherLocation?.humidity ?: 0.0,
                            viewState.weatherLocation?.dewPoint ?: 0.0,
                            modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                        )
                    }
                }
            }
            item {
                Row {
                    Column(Modifier.weight(1f)) {
                        VisibilityCard(
                            viewState.weatherLocation?.visibility ?: 0.0,
                            unit = viewState.weatherLocation?.unit ?: TemperaturePreference.METRIC,
                            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        UvIndexCard(
                            viewState.weatherLocation?.uvIndex ?: 0.0,
                            modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                        )
                    }
                }
            }
            item {
                viewState.weatherLocation?.let { weatherLocation ->
                    SunriseSunsetCard(
                        sunrise = weatherLocation.sunrise,
                        sunset = weatherLocation.sunset,
                        currentTime = weatherLocation.currentTime,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallScreenTopAppBar(
    title: String,
    onCloseClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    showDeleteButton: Boolean
) {
    WeatherYouSmallAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = WeatherYouTheme.typography.bodyLarge,
                color = WeatherYouTheme.colorScheme.onBackground,
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = WeatherYouTheme.colorScheme.primary,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            if (showDeleteButton) {
                IconButton(onClick = onDeleteButtonClick) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        tint = WeatherYouTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp),
                        contentDescription = stringResource(R.string.delete_location)
                    )
                }
            }
        },
    )
}

@Composable
fun ExpandedTopAppBar(
    title: String,
    onCloseClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    showDeleteButton: Boolean
) {
    WeatherYouLargeAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = WeatherYouTheme.typography.bodyLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    tint = WeatherYouTheme.colorScheme.primary,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            if (showDeleteButton) {
                IconButton(onClick = onDeleteButtonClick) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        tint = WeatherYouTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp),
                        contentDescription = stringResource(R.string.delete_location)
                    )
                }
            }
        },
        modifier = Modifier.animateContentSize()
    )
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
fun WeatherDetailsScreenPreview() {
    WeatherYouTheme {
        WeatherDetailsScreen(
            viewState = WeatherDetailsViewState(
                weatherLocation = PreviewWeatherLocation,
                todayWeatherHoursList = PreviewHourlyForecast,
                futureDaysList = PreviewFutureDaysForecast,
            ),
            isFullScreen = false,
            onExpandedButtonClick = { },
            onCloseClick = {},
            onDeleteClick = {}
        )
    }
}
