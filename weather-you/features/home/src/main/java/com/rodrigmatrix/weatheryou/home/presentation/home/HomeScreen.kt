package com.rodrigmatrix.weatheryou.home.presentation.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.core.compose.LaunchViewEffect
import com.rodrigmatrix.weatheryou.core.extensions.toast
import com.rodrigmatrix.weatheryou.domain.model.WeatherIcons
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.home.R
import com.rodrigmatrix.weatheryou.home.presentation.preview.PreviewWeatherList
import org.koin.androidx.compose.getViewModel

@Suppress("KotlinConstantConditions")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    bottomAppState: MutableState<Boolean>,
    onAddLocation: () -> Unit,
    expandedScreen: Boolean,
    viewModel: HomeViewModel = getViewModel(),
    locationPermissionState: PermissionState = rememberPermissionState(ACCESS_COARSE_LOCATION)
) {
    val viewState by viewModel.viewState.collectAsState()
    val context = LocalContext.current
    bottomAppState.value = viewState.isLocationSelected().not()
    BackHandler(enabled = viewState.isLocationSelected()) {
        viewModel.selectLocation(null)
    }
    LaunchViewEffect(viewModel) { viewEffect ->
        when (viewEffect) {
            is HomeViewEffect.Error -> context.toast(viewEffect.message)
        }
    }
    when {
        viewState.showLocationPermissionRequest(locationPermissionState) -> {
            HomeFabContent(
                expandedScreen = expandedScreen,
                onAddLocation = onAddLocation
            ) {
                RequestLocationPermission(
                    locationPermissionState,
                    onLocationPermissionChanged = {
                        viewModel.loadLocations()
                    }
                )
            }
        }
        expandedScreen -> {
            HomeScreenWithLocation(
                viewState = viewState,
                expandedScreen = expandedScreen,
                onItemClick = { weatherLocation ->
                    viewModel.selectLocation(weatherLocation)
                },
                onSwipeRefresh = viewModel::loadLocations,
                onCloseClick = viewModel::onCloseClicked,
                onDeleteLocation = { location ->
                    viewModel.deleteLocation(location)
                }
            )
        }
        else -> {
            AnimatedVisibility(
                visible = viewState.isLocationSelected(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                com.rodrigmatrix.weatheryou.locationdetails.presentaion.details.WeatherDetailsScreen(
                    weatherLocation = viewState.selectedWeatherLocation,
                    onCloseClick = {
                        viewModel.selectLocation(null)
                    },
                    expandedScreen = expandedScreen,
                    onDeleteLocation = {
                        viewState.selectedWeatherLocation?.let { viewModel.deleteLocation(it) }
                    }
                )
            }
            AnimatedVisibility(
                visible = viewState.isLocationSelected().not(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                HomeFabContent(
                    expandedScreen = expandedScreen,
                    onAddLocation = onAddLocation
                ) {
                    HomeScreen(
                        viewState = viewState,
                        expandedScreen = expandedScreen,
                        onItemClick = { weatherLocation ->
                            viewModel.selectLocation(weatherLocation)
                        },
                        onSwipeRefresh = viewModel::loadLocations,
                        onDeleteLocation = { location ->
                            viewModel.deleteLocation(location)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFabContent(
    expandedScreen: Boolean,
    onAddLocation: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            if (expandedScreen.not()) {
                LargeFloatingActionButton(
                    onClick = onAddLocation,
                    modifier = Modifier.padding(bottom = 80.dp),
                    shape = RoundedCornerShape(100)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        modifier = Modifier.size(24.dp),
                        contentDescription = stringResource(R.string.add_location)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        content()
    }
}

@Composable
fun HomeScreen(
    viewState: HomeViewState,
    expandedScreen: Boolean,
    onItemClick: (WeatherLocation) -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit
) {
    HomeScreenContent(
        viewState,
        expandedScreen,
        onItemClick,
        onSwipeRefresh,
        onDeleteLocation
    )
}

@Composable
fun HomeScreenContent(
    viewState: HomeViewState,
    expandedScreen: Boolean,
    onItemClick: (WeatherLocation) -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit
) {
    Surface(Modifier.fillMaxSize()) {
        when {
            viewState.isLoading.not() && viewState.locationsList.isEmpty() -> {
                WeatherLocationsEmptyState()
            }
            else -> {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(viewState.isLoading),
                    onRefresh = onSwipeRefresh,
                    swipeEnabled = viewState.locationsList.isNotEmpty()
                ) {
                    WeatherLocationList(
                        viewState.locationsList,
                        onItemClick = onItemClick,
                        onLongPress = onDeleteLocation,
                        contentPaddingValues = PaddingValues(
                            bottom = if (expandedScreen) 0.dp else 200.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreenWithLocation(
    viewState: HomeViewState,
    expandedScreen: Boolean,
    onItemClick: (WeatherLocation) -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onSwipeRefresh: () -> Unit,
    onCloseClick: () -> Unit
) {
    val detailsWeight: Float by animateFloatAsState(
        targetValue = if (viewState.isLocationSelected()) 1F else 0.1F,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing
        )
    )
    Row {
        Column(Modifier.weight(1f)) {
            HomeScreenContent(
                viewState,
                expandedScreen,
                onItemClick,
                onSwipeRefresh,
                onDeleteLocation
            )
        }
        if (viewState.isLocationSelected()) {
            Column(Modifier.weight(detailsWeight)) {
                com.rodrigmatrix.weatheryou.locationdetails.presentaion.details.WeatherDetailsScreen(
                    weatherLocation = viewState.selectedWeatherLocation,
                    onCloseClick = onCloseClick,
                    expandedScreen = true,
                    onDeleteLocation = {
                        viewState.selectedWeatherLocation?.let { onDeleteLocation(it) }
                    }
                )
            }
        }
    }
}

@Composable
fun WeatherLocationsEmptyState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
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
        Text(
            text = stringResource(R.string.add_location_description),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    permissionState: PermissionState = rememberPermissionState(ACCESS_COARSE_LOCATION),
    onLocationPermissionChanged: () -> Unit
) {
    when {
        permissionState.hasPermission -> {
            onLocationPermissionChanged()
        }
        else -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
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
                Button(onClick = { permissionState.launchPermissionRequest() }) {
                    Text(stringResource(R.string.grant_location_permission))
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(
            viewState = HomeViewState(locationsList = PreviewWeatherList),
            expandedScreen = false,
            { },
            { },
            { }
        )
    }
}

@Preview(device = Devices.PIXEL_C)
@Preview(device = Devices.PIXEL_C, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenWithLocationPreview() {
    MaterialTheme {
        HomeScreenWithLocation(
            viewState = HomeViewState(
                locationsList = PreviewWeatherList
            ),
            expandedScreen = false,
            { },
            { },
            { },
            { }
        )
    }
}