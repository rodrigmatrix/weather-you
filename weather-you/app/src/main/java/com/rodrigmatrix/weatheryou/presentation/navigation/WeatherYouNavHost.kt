package com.rodrigmatrix.weatheryou.presentation.navigation

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.rodrigmatrix.weatheryou.addlocation.AddLocationScreen
import com.rodrigmatrix.weatheryou.presentation.about.AboutScreen
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeScreen
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeEntry
import com.rodrigmatrix.weatheryou.home.presentation.navigation.NavigationEntries
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsScreen
import com.rodrigmatrix.weatheryou.presentation.utils.WeatherYouAppState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherHomeNavHost(
    appState: WeatherYouAppState,
    bottomAppState: MutableState<Boolean>,
    onAddLocationClick: () -> Unit,
    isExpandedScreen: Boolean
) {
    NavHost(
        appState.navController,
        startDestination = HomeEntry.Locations.route
    ) {
        composable(HomeEntry.Locations.route) {
            HomeScreen(
                bottomAppState,
                onAddLocation = onAddLocationClick,
                expandedScreen = isExpandedScreen
            )
        }
        composable(HomeEntry.Settings.route) {
            SettingsScreen()
        }
        composable(HomeEntry.About.route) {
            AboutScreen()
        }
        composable(NavigationEntries.ADD_LOCATION_ROUTE) {
            AddLocationScreen(appState.navController)
        }
    }
}