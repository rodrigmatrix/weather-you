package com.rodrigmatrix.weatheryou.presentation.details

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.components.WeatherYouLargeAppBar
import com.rodrigmatrix.weatheryou.presentation.components.WeatherYouSmallAppBar
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewFutureDaysForecast
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewHourlyForecast
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewWeatherLocation
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
        LazyColumn {
            item {
                viewState.weatherLocation?.let {
                    CurrentWeather(
                        it,
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 10.dp,
                                bottom = 10.dp
                            )
                            .focusable()
                    )
                }
            }
            item {
                HourlyForecast(
                    hoursList = viewState.todayWeatherHoursList,
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        )
                        .focusable()
                )
            }
            item {
                FutureDaysForecast(
                    futureDaysList = viewState.futureDaysList,
                    isExpanded = viewState.isFutureWeatherExpanded,
                    onExpandedButtonClick = onExpandedButtonClick,
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
    WeatherYouTheme {
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
    WeatherYouTheme {
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