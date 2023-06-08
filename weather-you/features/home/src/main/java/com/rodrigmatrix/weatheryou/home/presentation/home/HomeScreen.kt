package com.rodrigmatrix.weatheryou.home.presentation.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.ScreenContentType
import com.rodrigmatrix.weatheryou.components.ScreenNavigationType
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.domain.model.WeatherIcons
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.home.presentation.preview.PreviewWeatherList
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.details.WeatherDetailsScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    navigationType: ScreenNavigationType,
    displayFeatures: List<DisplayFeature>,
    contentType: ScreenContentType,
    onLocationSelected: (WeatherLocation?) -> Unit,
    onDismissLocationDialogClicked: () -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onDeleteLocationClicked: () -> Unit,
    onDeleteLocationConfirmButtonClicked: () -> Unit,
    onAddLocation: () -> Unit,
    locationPermissionState: PermissionState = rememberPermissionState(ACCESS_COARSE_LOCATION),
) {
    BackHandler(enabled = homeUiState.isLocationSelected()) {
        onLocationSelected(null)
    }

    if (homeUiState.deleteLocationDialogVisible) {
        DeleteLocationDialog(
            onDismiss = onDismissLocationDialogClicked,
            onDeleteLocationClicked = onDeleteLocationConfirmButtonClicked,
        )
    }

    HomeScreen(
        homeUiState = homeUiState,
        showLocationPermissionRequest = homeUiState.showLocationPermissionRequest(locationPermissionState),
        navigationType = navigationType,
        displayFeatures = displayFeatures,
        contentType = contentType,
        onLocationSelected = onLocationSelected,
        onSwipeRefresh = onSwipeRefresh,
        onDeleteLocation = onDeleteLocation,
        onDeleteLocationClicked = onDeleteLocationClicked,
        onAddLocation = onAddLocation,
        onRequestPermission = locationPermissionState::launchPermissionRequest,
    )
}

@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    showLocationPermissionRequest: Boolean,
    navigationType: ScreenNavigationType,
    displayFeatures: List<DisplayFeature>,
    contentType: ScreenContentType,
    onLocationSelected: (WeatherLocation?) -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onDeleteLocationClicked: () -> Unit,
    onAddLocation: () -> Unit,
    onRequestPermission: () -> Unit,
) {
    if (contentType == ScreenContentType.DUAL_PANE && homeUiState.isLocationSelected()) {
        TwoPane(
            first = {
                WeatherLocationsListScreen(
                    uiState = homeUiState,
                    isFullScreen = false,
                    showLocationPermissionRequest = showLocationPermissionRequest,
                    onItemClick = onLocationSelected,
                    onSwipeRefresh = onSwipeRefresh,
                    onDeleteLocation = onDeleteLocation,
                    onSearchLocationClick = onAddLocation,
                    onRequestPermission = onRequestPermission,
                )
            },
            second = {
                AnimatedVisibility(
                    visible = homeUiState.isLocationSelected(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    WeatherDetailsScreen(
                        weatherLocation = homeUiState.selectedWeatherLocation,
                        onCloseClick = {
                            onLocationSelected(null)
                        },
                        isFullScreen = false,
                        onDeleteLocationClicked = onDeleteLocationClicked
                    )
                }
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = displayFeatures
        )
    } else {
        WeatherLocationsListScreen(
            uiState = homeUiState,
            showLocationPermissionRequest = showLocationPermissionRequest,
            isFullScreen = false,
            onItemClick = onLocationSelected,
            onSwipeRefresh = onSwipeRefresh,
            onDeleteLocation = onDeleteLocation,
            onSearchLocationClick = onAddLocation,
            onRequestPermission = onRequestPermission,
        )
        AnimatedVisibility(
            visible = homeUiState.isLocationSelected(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            WeatherDetailsScreen(
                weatherLocation = homeUiState.selectedWeatherLocation,
                onCloseClick = {
                    onLocationSelected(null)
                },
                isFullScreen = false,
                onDeleteLocationClicked = onDeleteLocationClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherLocationsListScreen(
    uiState: HomeUiState,
    showLocationPermissionRequest: Boolean,
    isFullScreen: Boolean,
    onItemClick: (WeatherLocation) -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onSearchLocationClick: () -> Unit,
    onRequestPermission: () -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = onSwipeRefresh,
    )
    Scaffold(
        topBar = {
            SearchLocationBar(
                onSearchLocationClick = onSearchLocationClick,
            )
        }
    ) { paddingValues ->
        when {
            showLocationPermissionRequest -> {
                RequestLocationPermission(
                    onRequestPermission = onRequestPermission,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            uiState.isLoading.not() && uiState.locationsList.isEmpty() -> {
                WeatherLocationsEmptyState(
                    Modifier
                        .padding(paddingValues)
                )
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .pullRefresh(pullRefreshState)
                ) {
                    WeatherLocationList(
                        weatherLocationList = uiState.locationsList,
                        selectedLocation = uiState.selectedWeatherLocation,
                        onItemClick = onItemClick,
                        onDismiss = onDeleteLocation,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    PullRefreshIndicator(
                        refreshing = uiState.isLoading,
                        state = pullRefreshState,
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.secondaryContainer,
                        scale = true,
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherLocationsEmptyState(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 200.dp)
    ) {
        WeatherIcon(
            weatherIcons = WeatherIcons(
                com.rodrigmatrix.weatheryou.weathericons.R.raw.weather_cloudynight,
                com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_weather_cloudynight
            ),
            modifier = Modifier
                .size(120.dp)
                .padding(10.dp)
        )
        Text(
            text = stringResource(R.string.empty_locations),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun RequestLocationPermission(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 200.dp)
    ) {
        Image(
            imageVector = Icons.Filled.Place,
            contentDescription = stringResource(R.string.location_image),
            modifier = Modifier
                .size(120.dp)
                .padding(10.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Text(
            text = stringResource(R.string.enable_location),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = stringResource(R.string.enable_location_description),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = onRequestPermission,
            modifier = Modifier
        ) {
            Text(stringResource(R.string.grant_location_permission))
        }
    }
}

@Composable
fun DeleteLocationDialog(
    onDeleteLocationClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(com.rodrigmatrix.weatheryou.components.R.string.delete_location_title))
        },
        confirmButton = {
            TextButton(onClick = onDeleteLocationClicked) {
                Text(text = stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}


@Composable
fun SearchLocationBar(
    onSearchLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 4.dp, start = 16.dp, end = 16.dp),
        onClick = onSearchLocationClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.padding(start = 16.dp),
                tint = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = stringResource(id = com.rodrigmatrix.weatheryou.components.R.string.search_location),
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(
            homeUiState = HomeUiState(
                locationsList = PreviewWeatherList,
                selectedWeatherLocation = PreviewWeatherList.first(),
            ),
            showLocationPermissionRequest = false,
            navigationType = ScreenNavigationType.BOTTOM_NAVIGATION,
            displayFeatures = emptyList(),
            contentType = ScreenContentType.SINGLE_PANE,
            onLocationSelected = { },
            onSwipeRefresh = { },
            onDeleteLocation = { },
            onDeleteLocationClicked = { },
            onAddLocation = { },
            onRequestPermission = { },
        )
    }
}

@Preview(device = Devices.PIXEL_C)
@Preview(device = Devices.PIXEL_C, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenWithLocationPreview() {
    MaterialTheme {
        HomeScreen(
            homeUiState = HomeUiState(
                locationsList = PreviewWeatherList,
                selectedWeatherLocation = PreviewWeatherList.first(),
            ),
            showLocationPermissionRequest = false,
            navigationType = ScreenNavigationType.NAVIGATION_RAIL,
            displayFeatures = emptyList(),
            contentType = ScreenContentType.DUAL_PANE,
            onLocationSelected = { },
            onSwipeRefresh = { },
            onDeleteLocation = { },
            onDeleteLocationClicked = { },
            onAddLocation = { },
            onRequestPermission = { },
        )
    }
}