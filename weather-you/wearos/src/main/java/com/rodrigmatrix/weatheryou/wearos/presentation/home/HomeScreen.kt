package com.rodrigmatrix.weatheryou.wearos.presentation.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.rodrigmatrix.weatheryou.core.extensions.getHourWithMinutesString
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.wearos.R
import com.rodrigmatrix.weatheryou.wearos.presentation.components.CurvedText
import com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel.HomeViewModel
import com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel.HomeViewState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = getViewModel(),
    backgroundLocationPermissionState: PermissionState = rememberPermissionState(
        permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        } else {
            android.Manifest.permission.ACCESS_FINE_LOCATION
        },
        onPermissionResult = {
            //viewModel.on()
        }
    ),
    locationPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        onPermissionsResult = {
            if (it.all { it.value }) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //onDialogStateChanged(HomeDialogState.BackgroundLocation)
                } else {
                    //onPermissionGranted()
                }
            } else {
                //onPermissionGranted()
            }
        }
    ),
) {
    val viewState by viewModel.viewState.collectAsState()
    HomeScreen(
        viewState = viewState,
        locationPermissionState = locationPermissionState,
        onRefreshLocation = viewModel::loadLocation
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewState: HomeViewState,
    locationPermissionState: MultiplePermissionsState,
    onRefreshLocation: () -> Unit
) {
    when {
        locationPermissionState.allPermissionsGranted.not() -> {
            RequestLocationPermission(locationPermissionState, onRefreshLocation)
            return
        }
        viewState.isLoading -> {
            Loading()
            return
        }
        viewState.error != null -> {
            Error(
                error = viewState.error,
                onRefreshLocation = onRefreshLocation
            )
            return
        }
        viewState.weatherLocation != null -> {
            WeatherContent(viewState.weatherLocation)
        }
    }
}

@Composable
private fun Loading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(trackColor = MaterialTheme.colors.primary)
    }
}

@Composable
private fun Error(
    @StringRes error: Int,
    onRefreshLocation: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(error),
                style = MaterialTheme.typography.title3,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.padding(bottom = 4.dp))
            Button(
                modifier = Modifier.size(ButtonDefaults.SmallButtonSize),
                onClick = onRefreshLocation
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = stringResource(R.string.try_again)
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WeatherContent(
    weatherLocation: WeatherLocation
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScalingLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        timeText = {
            CurvedText(
                text = weatherLocation.currentTime.getHourWithMinutesString(context),
                style = MaterialTheme.typography.caption2
            )
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = scrollState)
        }
    ) {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        scrollState.scrollBy(it.verticalScrollPixels)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    true
                }
                .focusRequester(focusRequester)
                .focusable(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = scrollState
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                CurrentConditions(weatherLocation)
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    weatherLocation.hours.take(3).forEach {
                        WeatherHour(it)
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            item {
                UvIndex(uvIndex = weatherLocation.uvIndex)
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            item {
                SunriseSunset(
                    sunrise = weatherLocation.sunrise,
                    sunset = weatherLocation.sunset
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}