package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.layout.DisplayFeature
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rodrigmatrix.weatheryou.addlocation.AddLocationScreen
import com.rodrigmatrix.weatheryou.components.ScreenContentType
import com.rodrigmatrix.weatheryou.components.ScreenNavigationType
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.about.AboutScreen
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeScreen
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeUiState
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeEntry
import com.rodrigmatrix.weatheryou.home.presentation.navigation.NavigationEntries
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherHomeNavHost(
    contentType: ScreenContentType,
    displayFeatures: List<DisplayFeature>,
    homeUiState: HomeUiState,
    navigationType: ScreenNavigationType,
    onLocationSelected: (WeatherLocation?) -> Unit,
    onDismissLocationDialogClicked: () -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onDeleteLocationClicked: () -> Unit,
    onAddLocation: () -> Unit,
    onDeleteLocationConfirmButtonClicked: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController,
        startDestination = HomeEntry.Locations.route,
        modifier = modifier,
    ) {
        composable(HomeEntry.Locations.route) {
            HomeScreen(
                homeUiState = homeUiState,
                onAddLocation = onAddLocation,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationType = navigationType,
                onDismissLocationDialogClicked = onDismissLocationDialogClicked,
                onSwipeRefresh = onSwipeRefresh,
                onDeleteLocationClicked = onDeleteLocationClicked,
                onLocationSelected = onLocationSelected,
                onDeleteLocation = onDeleteLocation,
                onDeleteLocationConfirmButtonClicked = onDeleteLocationConfirmButtonClicked,
            )
        }
        composable(HomeEntry.Settings.route) {
            SettingsScreen()
        }
        composable(HomeEntry.About.route) {
            AboutScreen()
        }
        composable(NavigationEntries.ADD_LOCATION_ROUTE) {
            AddLocationScreen(navController)
        }
    }
}