package com.rodrigmatrix.weatheryou.tv.presentation.home

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.rodrigmatrix.weatheryou.components.DeleteLocationDialog
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.ScreenNavigationType
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.WeatherLocationCardContent
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.tv.components.TvCard
import com.rodrigmatrix.weatheryou.tv.presentation.details.TvWeatherLocationScreen
import com.rodrigmatrix.weatheryou.tv.presentation.locations.WeatherLocationsUiState
import com.rodrigmatrix.weatheryou.tv.presentation.locations.WeatherLocationsViewModel
import com.rodrigmatrix.weatheryou.tv.presentation.navigation.WeatherYouTvRoutes
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun TvWeatherLocationsScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherLocationsViewModel = getViewModel(),
    locationPermissionState: PermissionState = rememberPermissionState(Manifest.permission.ACCESS_COARSE_LOCATION),
    navController: NavController,
) {
    val uiState by viewModel.viewState.collectAsState()

    if (uiState.deleteLocationDialogVisible) {
        DeleteLocationDialog(
            onConfirmButtonClick = {
                viewModel.deleteLocation(ScreenNavigationType.NAVIGATION_RAIL)
            },
            onDismissButtonClick = viewModel::hideDeleteLocationDialog,
        )
    }
    TvWeatherLocationsScreen(
        uiState = uiState,
        showLocationPermissionRequest = uiState.showLocationPermissionRequest(locationPermissionState),
        onSearchLocationClick = {
            navController.navigate(WeatherYouTvRoutes.ADD_LOCATION_SCREEN)
        },
        onRequestPermission = locationPermissionState::launchPermissionRequest,
        onWeatherLocationClicked = viewModel::selectLocation,
        modifier = modifier,
    )
}

@Composable
private fun TvWeatherLocationsScreen(
    uiState: WeatherLocationsUiState,
    showLocationPermissionRequest: Boolean,
    onSearchLocationClick: () -> Unit,
    onRequestPermission: () -> Unit,
    onWeatherLocationClicked: (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SearchLocationBar(
            onSearchLocationClick = onSearchLocationClick,
        )
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
                TvWeatherLocationsContent(
                    locationsList = uiState.locationsList,
                    currentLocation = uiState.selectedWeatherLocation,
                    onWeatherLocationClicked = onWeatherLocationClicked,
                    modifier = Modifier,
                )
            }
        }
    }
}

@Composable
private fun TvWeatherLocationsContent(
    locationsList: List<WeatherLocation>,
    currentLocation: WeatherLocation?,
    onWeatherLocationClicked: (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier.padding(vertical = 8.dp)) {
        WeatherLocationsList(
            weatherLocationsList = locationsList,
            currentLocation = currentLocation,
            onWeatherLocationClicked = onWeatherLocationClicked,
            modifier = Modifier.weight(1f)
        )
        AnimatedVisibility(
            visible = currentLocation != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.weight(1f)
        ) {
            currentLocation?.let { selectedLocation ->
                TvWeatherLocationScreen(
                    weatherLocation = selectedLocation,
                    modifier = Modifier.weight(1f)
                )
            }
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
fun SearchLocationBar(
    onSearchLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = CircleShape.copy(CornerSize(20.dp))
    Surface(
        shape = ClickableSurfaceDefaults.shape(shape = CircleShape),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        scale = ClickableSurfaceDefaults.scale(
            scale = 1f,
            focusedScale = 1.01f
        ),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                shape = cardShape,
            )
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
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
                text = stringResource(id = R.string.search_location),
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
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
            weatherCondition = WeatherCondition.CLOUDY,
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
private fun WeatherLocationsList(
    weatherLocationsList: List<WeatherLocation>,
    currentLocation: WeatherLocation?,
    onWeatherLocationClicked: (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            Spacer(Modifier.height(4.dp))
        }
        items(weatherLocationsList) { weatherLocation ->
            TvCard(
                onClick = { onWeatherLocationClicked(weatherLocation) },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                WeatherLocationCardContent(weatherLocation = weatherLocation)
            }
        }
    }
}

