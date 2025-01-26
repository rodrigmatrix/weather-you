package com.rodrigmatrix.weatheryou.wearos.presentation.home

import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.wearos.R
import com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel.HomeViewModel
import com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel.HomeViewState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.TimeText
import com.rodrigmatrix.weatheryou.components.particle.WeatherAnimationsBackground
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition
import com.rodrigmatrix.weatheryou.wearos.presentation.components.WeatherIcon
import com.rodrigmatrix.weatheryou.wearos.presentation.components.pager.PagerScreen
import com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel.HomePage
import com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel.HomeViewEffect
import com.rodrigmatrix.weatheryou.wearos.presentation.navigation.WeatherYouNavigation

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
    navController: NavController,
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(initialPage = 1) {
        viewState.pages.size
    }
    HomeScreen(
        viewState = viewState,
        locationPermissionState = locationPermissionState,
        pagerState = pagerState,
        onRefreshLocation = viewModel::fetchLocations,
        onAddLocationChipClicked = {
            navController.navigate(WeatherYouNavigation.AddLocation.route)
        },
        onSettingsChipClicked = {
        },
        onEditLocationsChipClicked = {
            navController.navigate(WeatherYouNavigation.EditLocations.route)
        }
    )

    LaunchedEffect(Unit) {
        viewModel.viewEffect.collect { viewEffect ->
            when (viewEffect) {
                is HomeViewEffect.ScrollPager -> {
                    pagerState.scrollToPage(viewEffect.page)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewState: HomeViewState,
    locationPermissionState: MultiplePermissionsState,
    pagerState: PagerState,
    onRefreshLocation: () -> Unit,
    onAddLocationChipClicked: () -> Unit,
    onSettingsChipClicked: () -> Unit,
    onEditLocationsChipClicked: () -> Unit,
) {
    when {
        locationPermissionState.allPermissionsGranted.not() -> {
            RequestLocationPermission(locationPermissionState, onRefreshLocation)
        }
        viewState.isLoading -> {
            Loading()
        }
        viewState.error != null -> {
            Error(
                error = viewState.error,
                onRefreshLocation = onRefreshLocation
            )
        }
        else -> {
            PagerScreen(
                state = pagerState,
                timeText = {
                    TimeText()
                }
            ) { page ->
                val pageItem = viewState.pages[page]
                when (pageItem) {
                    HomePage.EmptyState -> {
                        EmptyState()
                    }
                    HomePage.Settings -> {
                        HomeSettings(
                            onAddLocationChipClicked = onAddLocationChipClicked,
                            onSettingsChipClicked = onSettingsChipClicked,
                            onEditLocationsChipClicked = onEditLocationsChipClicked,
                        )
                    }
                    is HomePage.Weather -> {
                        WeatherContent(pageItem.weatherLocation)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeSettings(
    onAddLocationChipClicked: () -> Unit,
    onEditLocationsChipClicked: () -> Unit,
    onSettingsChipClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScalingLazyColumn(
        modifier = modifier.fillMaxWidth(),
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
        item {
            Chip(
                onClick = onAddLocationChipClicked,
                colors = ChipDefaults.secondaryChipColors(),
                label = {
                    Text(
                        text = "Add Location",
                        maxLines = 3, overflow = TextOverflow.Ellipsis
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_add_location),
                        contentDescription = "add location",
                        modifier = Modifier
                            .size(ChipDefaults.IconSize)
                            .wrapContentSize(align = Alignment.Center),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            Chip(
                onClick = onEditLocationsChipClicked,
                colors = ChipDefaults.secondaryChipColors(),
                label = {
                    Text(
                        text = "Manage Locations",
                        maxLines = 3, overflow = TextOverflow.Ellipsis
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_edit_location),
                        contentDescription = "reorder",
                        modifier = Modifier
                            .size(ChipDefaults.IconSize)
                            .wrapContentSize(align = Alignment.Center),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            Chip(
                onClick = onSettingsChipClicked,
                colors = ChipDefaults.secondaryChipColors(),
                label = {
                    Text(
                        text = "Settings",
                        maxLines = 3, overflow = TextOverflow.Ellipsis
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_settings),
                        contentDescription = "settings",
                        modifier = Modifier
                            .size(ChipDefaults.IconSize)
                            .wrapContentSize(align = Alignment.Center),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
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

@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(16.dp))
        WeatherIcon(
            weatherCondition = WeatherCondition.MostlyClear,
            isDaylight = true,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = "No locations found. Please add locations scrolling to the left.",
            textAlign = TextAlign.Center,
        )
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
        positionIndicator = {
            PositionIndicator(scalingLazyListState = scrollState)
        }
    ) {
        Box {
            WeatherAnimationsBackground(
                weatherLocation,
            )
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
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}
