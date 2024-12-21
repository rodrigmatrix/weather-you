@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.rodrigmatrix.weatheryou.home.presentation.home

import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.extensions.getGradientList
import com.rodrigmatrix.weatheryou.components.location.RequestBackgroundLocationDialog
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherList
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.details.WeatherDetailsScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeUiState: HomeUiState,
    navigator: ThreePaneScaffoldNavigator<Int>,
    onLocationSelected: (WeatherLocation?) -> Unit,
    onDialogStateChanged: (HomeDialogState) -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onDeleteLocationConfirmButtonClicked: () -> Unit,
    onAddLocation: () -> Unit,
    onPermissionGranted: () -> Unit,
    onOrderChanged: (List<WeatherLocation>) -> Unit,
    backgroundLocationPermissionState: PermissionState = rememberPermissionState(
        permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        } else {
            android.Manifest.permission.ACCESS_FINE_LOCATION
        },
        onPermissionResult = {
            onPermissionGranted()
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
                    onDialogStateChanged(HomeDialogState.BackgroundLocation)
                } else {
                    onPermissionGranted()
                }
            } else {
                onPermissionGranted()
            }
        }
    ),
    onNavigateToLocation: (Int) -> Unit,
) {

    BackHandler(enabled = homeUiState.isLocationSelected()) {
        onLocationSelected(null)
    }

    when (homeUiState.dialogState) {
        HomeDialogState.BackgroundLocation -> {
            RequestBackgroundLocationDialog(
                onRequestPermissionClicked = {
                    backgroundLocationPermissionState.launchPermissionRequest()
                    onDialogStateChanged(HomeDialogState.Hidden)
                },
                onDismissRequest = {
                    onPermissionGranted()
                    onDialogStateChanged(HomeDialogState.Hidden)
                },
            )
        }
        HomeDialogState.DeleteLocation -> {
            DeleteLocationDialog(
                onDismiss = {
                    onDialogStateChanged(HomeDialogState.Hidden)
                },
                onDeleteLocationClicked = onDeleteLocationConfirmButtonClicked,
            )
        }
        HomeDialogState.Hidden -> Unit
    }
    HomeScreen(
        homeUiState = homeUiState,
        navigator = navigator,
        showLocationPermissionRequest = homeUiState.showLocationPermissionRequest(locationPermissionState),
        onLocationSelected = onLocationSelected,
        onSwipeRefresh = onSwipeRefresh,
        onDeleteLocation = onDeleteLocation,
        onDeleteLocationClicked = {
            onDialogStateChanged(HomeDialogState.DeleteLocation)
        },
        onAddLocation = onAddLocation,
        onRequestPermission = locationPermissionState::launchMultiplePermissionRequest,
        onOrderChanged = onOrderChanged,
        onNavigateToLocation = onNavigateToLocation,
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WeatherLocationsListScreen(
    uiState: HomeUiState,
    navigator: ThreePaneScaffoldNavigator<Int>,
    showLocationPermissionRequest: Boolean,
    onItemClick: (WeatherLocation) -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onSearchLocationClick: () -> Unit,
    onRequestPermission: () -> Unit,
    onOrderChanged: (List<WeatherLocation>) -> Unit,
    onNavigateToLocation: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = onSwipeRefresh,
    )

    Box {
        Scaffold(
            topBar = {
                SearchLocationBar(
                    onSearchLocationClick = onSearchLocationClick,
                )
            },
            containerColor = Color.Transparent,
            modifier = modifier.background(Color.Transparent)
        ) { paddingValues ->
            Box(
//            modifier = Modifier.background(brush = if (uiState.enableWeatherAnimations && uiState.selectedWeatherLocation != null) {
//            Brush.linearGradient(uiState.selectedWeatherLocation.getGradientList().map {
//                it.copy(alpha = 0.4f)
//            })
//            } else {
//                Brush.linearGradient(listOf(WeatherYouTheme.colorScheme.background, WeatherYouTheme.colorScheme.background))
//            })
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
            ) {
                when {
                    showLocationPermissionRequest -> {
                        RequestLocationPermission(
                            onRequestPermission = onRequestPermission,
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
                                .pullRefresh(pullRefreshState)
                        ) {
                            WeatherLocationList(
                                weatherLocationList = uiState.locationsList,
                                isRefreshingLocations = uiState.isRefreshingLocations,
                                selectedLocation = uiState.selectedWeatherLocation,
                                onItemClick = onItemClick,
                                onDismiss = onDeleteLocation,
                                onOrderChanged = onOrderChanged,
                                modifier = Modifier,
                            )

                            PullRefreshIndicator(
                                refreshing = uiState.isLoading,
                                state = pullRefreshState,
                                backgroundColor = WeatherYouTheme.colorScheme.primary,
                                contentColor = WeatherYouTheme.colorScheme.secondaryContainer,
                                scale = true,
                                modifier = Modifier.align(Alignment.TopCenter),
                            )
                        }
                    }
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    showLocationPermissionRequest: Boolean,
    navigator: ThreePaneScaffoldNavigator<Int>,
    onLocationSelected: (WeatherLocation?) -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onDeleteLocationClicked: () -> Unit,
    onAddLocation: () -> Unit,
    onRequestPermission: () -> Unit,
    onOrderChanged: (List<WeatherLocation>) -> Unit,
    onNavigateToLocation: (Int) -> Unit,
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val navSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
        onLocationSelected(null)
    }

    ListDetailPaneScaffold(
        value = navigator.scaffoldValue,
        directive = navigator.scaffoldDirective,
        listPane = {
            AnimatedPane(
                modifier = Modifier
                    .preferredWidth(260.dp)
                    .statusBarsPadding()
            ) {
                WeatherLocationsListScreen(
                    uiState = homeUiState,
                    navigator = navigator,
                    showLocationPermissionRequest = showLocationPermissionRequest,
                    onItemClick = {
                        onLocationSelected(it)
                        onNavigateToLocation(it.id)
                    },
                    onSwipeRefresh = onSwipeRefresh,
                    onDeleteLocation = onDeleteLocation,
                    onSearchLocationClick = onAddLocation,
                    onRequestPermission = onRequestPermission,
                    onOrderChanged = onOrderChanged,
                    onNavigateToLocation = onNavigateToLocation,
                )
            }
        },
        detailPane = {
            homeUiState.selectedWeatherLocation?.let {
                AnimatedPane {
                    WeatherDetailsScreen(
                        weatherLocation = homeUiState.selectedWeatherLocation,
                        isUpdating = homeUiState.isRefreshingLocations,
                        onCloseClick = {
                            navigator.navigateBack()
                            onLocationSelected(null)
                        },
                        onDeleteLocationClicked = onDeleteLocationClicked,
                    )
                }
            }
        },
        modifier = if (WeatherYouTheme.themeSettings.showWeatherAnimations && navSuiteType == NavigationSuiteType.NavigationRail) {
            Modifier.background(
                Brush.verticalGradient(
                    homeUiState.getSelectedOrFirstLocation()?.getGradientList() ?: listOf(
                        WeatherYouTheme.colorScheme.background,
                        WeatherYouTheme.colorScheme.background,
                    )
                )
            )
        } else {
            Modifier.background(WeatherYouTheme.colorScheme.background)
        },
    )
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
            weatherCondition = WeatherCondition.PartlyCloudy,
            isDaylight = false,
            modifier = Modifier
                .size(120.dp)
                .padding(10.dp)
        )
        Text(
            text = stringResource(R.string.empty_locations),
            style = WeatherYouTheme.typography.headlineSmall,
            color = WeatherYouTheme.colorScheme.onBackground,
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
            colorFilter = ColorFilter.tint(WeatherYouTheme.colorScheme.primary)
        )
        Text(
            text = stringResource(R.string.enable_location),
            style = WeatherYouTheme.typography.headlineSmall,
            color = WeatherYouTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = stringResource(R.string.enable_location_description),
            style = WeatherYouTheme.typography.titleSmall,
            color = WeatherYouTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = onRequestPermission,
            modifier = Modifier
        ) {
            Text(
                text = stringResource(R.string.grant_location_permission),
                style = WeatherYouTheme.typography.titleSmall,
                color = WeatherYouTheme.colorScheme.onPrimary,
            )
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
            Text(text = stringResource(R.string.delete_location_title))
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
        color = WeatherYouTheme.colorScheme.secondaryContainer,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
        onClick = onSearchLocationClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.padding(start = 16.dp),
                tint = WeatherYouTheme.colorScheme.primary,
            )

            Text(
                text = stringResource(id = com.rodrigmatrix.weatheryou.components.R.string.search_location),
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                style = WeatherYouTheme.typography.bodyMedium,
                color = WeatherYouTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    WeatherYouTheme {
        HomeScreen(
            homeUiState = HomeUiState(
                locationsList = PreviewWeatherList,
                selectedWeatherLocation = PreviewWeatherList.first(),
            ),
            navigator = rememberListDetailPaneScaffoldNavigator<Int>(
                calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(currentWindowAdaptiveInfo())
            ),
            showLocationPermissionRequest = false,
            onLocationSelected = { },
            onSwipeRefresh = { },
            onDeleteLocation = { },
            onDeleteLocationClicked = { },
            onAddLocation = { },
            onRequestPermission = { },
            onOrderChanged = { },
            onNavigateToLocation = { }
        )
    }
}

@Preview(device = Devices.PIXEL_C)
@Preview(device = Devices.PIXEL_C, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenWithLocationPreview() {
    WeatherYouTheme {
        HomeScreen(
            homeUiState = HomeUiState(
                locationsList = PreviewWeatherList,
                selectedWeatherLocation = PreviewWeatherList.first(),
            ),
            navigator = rememberListDetailPaneScaffoldNavigator<Int>(
                calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(currentWindowAdaptiveInfo())
            ),
            showLocationPermissionRequest = false,
            onLocationSelected = { },
            onSwipeRefresh = { },
            onDeleteLocation = { },
            onDeleteLocationClicked = { },
            onAddLocation = { },
            onRequestPermission = { },
            onOrderChanged = { },
            onNavigateToLocation = { }
        )
    }
}