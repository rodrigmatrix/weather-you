package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.rodrigmatrix.weatheryou.components.DevicePosture
import com.rodrigmatrix.weatheryou.components.ScreenContentType
import com.rodrigmatrix.weatheryou.components.ScreenNavigationContentPosition
import com.rodrigmatrix.weatheryou.components.ScreenNavigationType
import com.rodrigmatrix.weatheryou.components.isBookPosture
import com.rodrigmatrix.weatheryou.components.isSeparating
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeUiState
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeViewModel
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeEntry
import com.rodrigmatrix.weatheryou.home.presentation.navigation.NavigationEntries
import com.rodrigmatrix.weatheryou.presentation.utils.ScreenNavigationActions
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeNavigationScreen(
    viewModel: HomeViewModel = getViewModel(),
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
) {
    /**
     * This will help us select type of navigation and content type depending on window size and
     * fold state of the device.
     */
    val homeUiState by viewModel.viewState.collectAsState()
    val navController = rememberNavController()

    val navigationType: ScreenNavigationType
    val contentType: ScreenContentType

    /**
     * We are using display's folding features to map the device postures a fold is in.
     * In the state of folding device If it's half fold in BookPosture we want to avoid content
     * at the crease/hinge
     */
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)

        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

        else -> DevicePosture.NormalPosture
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = ScreenNavigationType.BOTTOM_NAVIGATION
            contentType = ScreenContentType.SINGLE_PANE
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = ScreenNavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                ScreenContentType.DUAL_PANE
            } else {
                ScreenContentType.SINGLE_PANE
            }
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                ScreenNavigationType.NAVIGATION_RAIL
            } else {
                ScreenNavigationType.NAVIGATION_RAIL
            }
            contentType = ScreenContentType.DUAL_PANE
        }
        else -> {
            navigationType = ScreenNavigationType.BOTTOM_NAVIGATION
            contentType = ScreenContentType.SINGLE_PANE
        }
    }

    /**
     * Content inside Navigation Rail/Drawer can also be positioned at top, bottom or center for
     * ergonomics and reachability depending upon the height of the device.
     */
    val navigationContentPosition = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            ScreenNavigationContentPosition.TOP
        }
        WindowHeightSizeClass.Medium,
        WindowHeightSizeClass.Expanded -> {
            ScreenNavigationContentPosition.CENTER
        }
        else -> {
            ScreenNavigationContentPosition.TOP
        }
    }

    ScreenNavigationWrapper(
        homeUiState = homeUiState,
        navController = navController,
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition,
        onDismissLocationDialogClicked = viewModel::hideDeleteLocationDialog,
        onDeleteLocationClicked = viewModel::showDeleteLocationDialog,
        onDeleteLocation = { location ->
            viewModel.deleteLocation(
                location = location,
                setSelectedLocation = navigationType != ScreenNavigationType.BOTTOM_NAVIGATION
            )
        },
        onSwipeRefresh = viewModel::loadLocations,
        onLocationSelected = viewModel::selectLocation,
        onAddLocation = {
            navController.navigate(NavigationEntries.ADD_LOCATION_ROUTE)
        },
        onDeleteLocationConfirmButtonClicked = viewModel::deleteLocation
    )
}

@Composable
private fun ScreenNavigationWrapper(
    homeUiState: HomeUiState,
    navController: NavHostController,
    navigationType: ScreenNavigationType,
    contentType: ScreenContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: ScreenNavigationContentPosition,
    onLocationSelected: (WeatherLocation?) -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onDismissLocationDialogClicked: () -> Unit,
    onDeleteLocationClicked: () -> Unit,
    onSwipeRefresh: () -> Unit,
    onAddLocation: () -> Unit,
    onDeleteLocationConfirmButtonClicked: () -> Unit,
) {
    val navigationActions = remember(navController) {
        ScreenNavigationActions(navController)
    }

    ScreenAppContent(
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition,
        homeUiState = homeUiState,
        navController = navController,
        onDismissLocationDialogClicked = onDismissLocationDialogClicked,
        onSwipeRefresh = onSwipeRefresh,
        onDeleteLocationClicked = onDeleteLocationClicked,
        onDeleteLocation = onDeleteLocation,
        onLocationSelected = onLocationSelected,
        onAddLocation = onAddLocation,
        navigateToTopLevelDestination = navigationActions::navigateTo,
        onDeleteLocationConfirmButtonClicked = onDeleteLocationConfirmButtonClicked,
    )
}

@Composable
fun ScreenAppContent(
    homeUiState: HomeUiState,
    navigationType: ScreenNavigationType,
    contentType: ScreenContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: ScreenNavigationContentPosition,
    navController: NavHostController,
    navigateToTopLevelDestination: (HomeEntry) -> Unit,
    onLocationSelected: (WeatherLocation?) -> Unit,
    onDismissLocationDialogClicked: () -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onDeleteLocationClicked: () -> Unit,
    onDeleteLocationConfirmButtonClicked: () -> Unit,
    onAddLocation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == ScreenNavigationType.NAVIGATION_RAIL) {
            HomeNavigationRail(
                navigationContentPosition = navigationContentPosition,
                onNavigationItemClick = navigateToTopLevelDestination,
                navController = navController,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            WeatherHomeNavHost(
                contentType = contentType,
                displayFeatures = displayFeatures,
                homeUiState = homeUiState,
                navigationType = navigationType,
                onDismissLocationDialogClicked = onDismissLocationDialogClicked,
                onSwipeRefresh = onSwipeRefresh,
                onDeleteLocationClicked = onDeleteLocationClicked,
                onDeleteLocation = onDeleteLocation,
                onLocationSelected = onLocationSelected,
                onAddLocation = onAddLocation,
                onDeleteLocationConfirmButtonClicked = onDeleteLocationConfirmButtonClicked,
                navController = navController,
                modifier = Modifier.weight(1f),
            )

            AnimatedVisibility(
                visible = navigationType == ScreenNavigationType.BOTTOM_NAVIGATION &&
                    homeUiState.isLocationSelected().not() && HomeEntry.values().any {
                    navController.currentDestination?.route == it.route
                }
            ) {
                HomeBottomBar(
                    navController,
                    onNavigationItemClick = {
                        navigateToTopLevelDestination(it)
                    }
                )
            }
        }
    }
}


