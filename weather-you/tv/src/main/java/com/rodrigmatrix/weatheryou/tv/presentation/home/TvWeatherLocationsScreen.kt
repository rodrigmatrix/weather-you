package com.rodrigmatrix.weatheryou.tv.presentation.home

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ListItem
import androidx.tv.material3.Text
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.WeatherLocationCardContent
import com.rodrigmatrix.weatheryou.components.extensions.shimmerLoadingAnimation
import com.rodrigmatrix.weatheryou.components.particle.WeatherAnimationsBackground
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeDialogState
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeUiState
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeViewModel
import com.rodrigmatrix.weatheryou.tv.components.TvCard
import com.rodrigmatrix.weatheryou.tv.presentation.details.TvWeatherDetailsScreen
import com.rodrigmatrix.weatheryou.tv.presentation.details.TvWeatherLocationScreen
import com.rodrigmatrix.weatheryou.tv.presentation.locations.TvWeatherLocationsUiState
import com.rodrigmatrix.weatheryou.tv.presentation.locations.TVWeatherLocationsViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun TvWeatherLocationsScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    locationPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        onPermissionsResult = {
            viewModel.updateLocations()
        }
    ),
    navController: NavController,
) {
    val uiState by viewModel.viewState.collectAsState()
    var locationToDelete: WeatherLocation? by remember { mutableStateOf(null) }

    when (uiState.dialogState) {
        HomeDialogState.BackgroundLocation -> Unit
        HomeDialogState.DeleteLocation -> DeleteLocationDialog(
            onConfirmButtonClick = {
                locationToDelete?.let { viewModel.deleteLocation(it) }
            },
            onDismissButtonClick = {
                viewModel.onDialogStateChanged(HomeDialogState.Hidden)
            },
        )
        HomeDialogState.Hidden -> Unit
    }
    TvWeatherLocationsScreen(
        uiState = uiState,
        showLocationPermissionRequest = uiState.showLocationPermissionRequest(locationPermissionState),
        onRequestPermission = locationPermissionState::launchMultiplePermissionRequest,
        onWeatherLocationClicked = viewModel::selectLocation,
        onDeleteLocation = {
            locationToDelete = it
            viewModel.onDialogStateChanged(HomeDialogState.DeleteLocation)
        },
        modifier = modifier,
    )
}

@Composable
private fun TvWeatherLocationsScreen(
    uiState: HomeUiState,
    showLocationPermissionRequest: Boolean,
    onRequestPermission: () -> Unit,
    onWeatherLocationClicked: (WeatherLocation) -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        when {
            showLocationPermissionRequest -> {
                RequestLocationPermission(
                    onRequestPermission = onRequestPermission,
                    modifier = Modifier,
                )
            }

            uiState.isLoading.not() && uiState.locationsList.isEmpty() -> {
                WeatherLocationsEmptyState(
                    modifier = Modifier,
                )
            }

            else -> {
                if (uiState.locationsList.isEmpty()) {
                    WeatherLocationsLoadingState(
                        size = 0
                    )
                } else {
                    TvWeatherLocationsContent(
                        locationsList = uiState.locationsList,
                        currentLocation = uiState.selectedWeatherLocation,
                        onWeatherLocationClicked = onWeatherLocationClicked,
                        onDeleteLocation = onDeleteLocation,
                        modifier = Modifier,
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherLocationsLoadingState(
    size: Int,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            Spacer(Modifier.height(4.dp))
        }
        items(size) {
            TvCard(
                onClick = { },
                onLongClick = { },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .fillMaxHeight()
                ) {
                    Column(Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .height(40.dp)
                                .padding(bottom = 8.dp)
                                .padding(horizontal = 16.dp)
                                .shimmerLoadingAnimation()
                        )
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .height(20.dp)
                                .padding(bottom = 8.dp)
                                .padding(horizontal = 16.dp)
                                .shimmerLoadingAnimation()
                        )
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .height(40.dp)
                                .padding(horizontal = 16.dp)
                                .shimmerLoadingAnimation()
                        )

                    }
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .size(60.dp)
                            .clip(CircleShape)

                            .align(Alignment.CenterVertically)
                            .shimmerLoadingAnimation()
                    )
                }
            }
        }
    }
}

@Composable
private fun TvWeatherLocationsContent(
    locationsList: List<WeatherLocation>,
    currentLocation: WeatherLocation?,
    onWeatherLocationClicked: (WeatherLocation) -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
    ) {
        WeatherLocationsList(
            weatherLocationsList = locationsList,
            currentLocation = currentLocation,
            onWeatherLocationClicked = onWeatherLocationClicked,
            onDeleteLocation = onDeleteLocation,
            modifier = Modifier.weight(1f)
        )
        currentLocation?.let { selectedLocation ->
            TvWeatherDetailsScreen(
                weatherLocation = selectedLocation,
                modifier = Modifier.weight(1f),
            )
        }
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
            colorFilter = ColorFilter.tint(WeatherYouTheme.colorScheme.primary)
        )
        Text(
            text = stringResource(R.string.enable_location),
            color = WeatherYouTheme.colorScheme.onSurface,
            style = WeatherYouTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = stringResource(R.string.enable_location_description),
            style = WeatherYouTheme.typography.titleSmall,
            color = WeatherYouTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = onRequestPermission,
            colors = ButtonDefaults.colors(
                containerColor = WeatherYouTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier
        ) {
            Text(
                text = stringResource(R.string.grant_location_permission),
            )
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
            weatherCondition = WeatherCondition.Cloudy,
            isDaylight = true,
            modifier = Modifier
                .size(120.dp)
                .padding(10.dp)
        )
        Text(
            text = stringResource(R.string.empty_locations),
            style = WeatherYouTheme.typography.headlineSmall,
            color = WeatherYouTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
private fun WeatherLocationsList(
    weatherLocationsList: List<WeatherLocation>,
    currentLocation: WeatherLocation?,
    onWeatherLocationClicked: (WeatherLocation) -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(),
    ) {
        item {
            Spacer(Modifier.height(4.dp))
        }
        items(weatherLocationsList) { weatherLocation ->
            TvCard(
                onClick = { onWeatherLocationClicked(weatherLocation) },
                onLongClick = {
                    if (!weatherLocation.isCurrentLocation) {
                        onDeleteLocation(weatherLocation)
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Box {
                    if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
                        WeatherAnimationsBackground(
                            weatherLocation = weatherLocation,
                            modifier = Modifier.height(130.dp),
                        )
                    }
                    WeatherLocationCardContent(
                        weatherLocation = weatherLocation,
                        isRefreshingLocations = false,
                    )
                }
            }
        }
    }
}


@Composable
fun DeleteLocationDialog(
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissButtonClick) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(WeatherYouTheme.colorScheme.secondaryContainer)
                .padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.delete_location_title),
                color = WeatherYouTheme.colorScheme.onSecondaryContainer,
                style = WeatherYouTheme.typography.titleMedium,
                modifier = Modifier.focusable(enabled = true),
            )
            Spacer(Modifier.height(16.dp))
            ListItem(
                selected = false,
                onClick = onConfirmButtonClick,
                headlineContent = {

                },
                leadingContent = {
                    Text(
                        text = stringResource(R.string.delete),
                        style = WeatherYouTheme.typography.titleMedium,
                    )
                }
            )
            ListItem(
                selected = false,
                onClick = onDismissButtonClick,
                leadingContent = {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = WeatherYouTheme.typography.titleMedium,
                    )
                },
                headlineContent = {

                },
            )
        }
    }
}
