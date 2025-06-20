package com.rodrigmatrix.weatheryou.presentation.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.updateAll
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherList
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.core.extensions.getHourWithMinutesString
import com.rodrigmatrix.weatheryou.core.extensions.getTimeZoneHourAndMinutes
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.details.SmallScreenTopAppBar
import com.rodrigmatrix.weatheryou.presentation.widget.CurrentWeatherWidgetConfigurationUiAction.OnConfigurationCompleted
import com.rodrigmatrix.weatheryou.widgets.weather.CurrentWeatherWidget
import com.rodrigmatrix.weatheryou.widgets.weather.animated.CurrentAnimatedWeatherWidget
import com.rodrigmatrix.weatheryou.worker.UpdateWidgetWeatherDataWorker
import org.koin.androidx.compose.koinViewModel

@ExperimentalMaterial3Api
@Composable
fun CurrentWeatherWidgetConfigurationScreen(
    widgetId: String,
    modifier: Modifier = Modifier,
    viewModel: CurrentWeatherWidgetConfigurationViewModel = koinViewModel(),
    onConfigurationCancelled: () -> Unit,
    onConfigurationComplete: () -> Unit,
) {
    val uiState by viewModel.viewState.collectAsState()
    val context = LocalContext.current

    CurrentWeatherWidgetConfigurationScreen(
        uiState = uiState,
        onLocationClicked = { viewModel.setLocation(it, widgetId) },
        onCloseClick = onConfigurationCancelled,
        modifier = modifier,
    )
    BackHandler {
        onConfigurationCancelled()
    }
    LaunchedEffect(Unit) {
        viewModel.getLocations(widgetId)
    }
    LaunchedEffect(viewModel) {
        viewModel.viewEffect.collect { uiAction ->
            when (uiAction) {
                OnConfigurationCompleted -> {
                    onConfigurationComplete()
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun CurrentWeatherWidgetConfigurationScreen(
    uiState: CurrentWeatherWidgetConfigurationUiState,
    onLocationClicked : (WeatherLocation) -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SmallScreenTopAppBar(
            title = stringResource(R.string.widget_location),
            onCloseClick = onCloseClick,
            onDeleteButtonClick = { },
            onFullScreenModeChange = { },
            isFullScreenMode = false,
            showDeleteButton = false,
        )
        if (uiState.isLoading) {
            Loading()
        } else {
            CurrentWeatherWidgetConfigurationContent(
                locationsList = uiState.locationsList,
                selectedLocation = uiState.selectedLocation,
                onLocationClicked = onLocationClicked,
            )
        }
    }
}

@Composable
private fun Loading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(color = WeatherYouTheme.colorScheme.primary)
    }
}

@Composable
private fun CurrentWeatherWidgetConfigurationContent(
    locationsList: List<WeatherLocation>,
    selectedLocation: WeatherLocation?,
    onLocationClicked : (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.weather_widget_disclaimer),
            style = WeatherYouTheme.typography.bodyLarge,
            color = WeatherYouTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 24.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ) {
            items(locationsList) { weatherLocation ->
                LocationRow(
                    weatherLocation = weatherLocation,
                    isSelectedLocation = weatherLocation == selectedLocation,
                    onLocationClicked = onLocationClicked,
                )
            }
        }
    }
}

@Composable
private fun LocationRow(
    weatherLocation: WeatherLocation,
    isSelectedLocation: Boolean,
    onLocationClicked : (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Surface(
        checked = isSelectedLocation,
        shape = RoundedCornerShape(24.dp),
        color = WeatherYouTheme.colorScheme.secondaryContainer,
        border = if (isSelectedLocation)
            BorderStroke(2.dp, WeatherYouTheme.colorScheme.primary)
        else null,
        onCheckedChange = { onLocationClicked(weatherLocation) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .fillMaxHeight()
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = weatherLocation.currentWeather.temperatureString(),
                    style = WeatherYouTheme.typography.headlineLarge,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
                Text(
                    text = weatherLocation.timeZone.getTimeZoneHourAndMinutes(context).ifEmpty {
                        weatherLocation.currentTime.getHourWithMinutesString(context)
                    },
                    style = WeatherYouTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
                Text(
                    text = weatherLocation.name,
                    style = WeatherYouTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .height(100.dp),
            ) {
                WeatherIcon(
                    weatherCondition = weatherLocation.currentCondition,
                    isDaylight = weatherLocation.isDaylight,
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center)
                )
                if (weatherLocation.isCurrentLocation) {
                    Icon(
                        painter = painterResource(R.drawable.ic_my_location),
                        tint = WeatherYouTheme.colorScheme.primary,
                        contentDescription = stringResource(R.string.current_location),
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }
                if (isSelectedLocation) {
                    Surface(
                        color = WeatherYouTheme.colorScheme.secondary,
                        shape = CircleShape,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(24.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Icon(
                            painter = painterResource(id = com.rodrigmatrix.weatheryou.R.drawable.ic_check),
                            tint = WeatherYouTheme.colorScheme.onSecondary,
                            contentDescription = null,
                            modifier = Modifier
                                .size(18.dp),
                        )
                    }
                }
            }
        }
    }
}


@ExperimentalMaterial3Api
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(device = Devices.PIXEL_C)
@Composable
private fun CurrentWeatherWidgetConfigurationPreview() {
    WeatherYouTheme {
        CurrentWeatherWidgetConfigurationScreen(
            uiState = CurrentWeatherWidgetConfigurationUiState(
                locationsList = PreviewWeatherList,
                selectedLocation = PreviewWeatherList.first(),
            ),
            onLocationClicked = { },
            onCloseClick = { },
            modifier = Modifier.fillMaxSize()
        )
    }
}