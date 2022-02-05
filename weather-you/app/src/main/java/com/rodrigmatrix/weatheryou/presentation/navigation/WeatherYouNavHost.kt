package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.about.AboutScreen
import com.rodrigmatrix.weatheryou.presentation.addLocation.AddLocationScreen
import com.rodrigmatrix.weatheryou.presentation.details.WeatherDetailsScreen
import com.rodrigmatrix.weatheryou.presentation.home.HomeScreen
import com.rodrigmatrix.weatheryou.presentation.settings.SettingsScreen

@Composable
fun WeatherHomeNavHost(
    navController: NavHostController,
    onWeatherLocationClick: (WeatherLocation) -> Unit,
    onAddLocationClick: () -> Unit,
    isExpandedScreen: Boolean,
    weatherLocation: WeatherLocation?
) {
    NavHost(
        navController,
        startDestination = HomeEntry.Home.route
    ) {
        composable(HomeEntry.Home.route) {
            HomeScreen(
                onItemClick = { onWeatherLocationClick(it) },
                onAddLocation = onAddLocationClick,
                showFab = isExpandedScreen.not()
            )
        }
        composable(NavigationEntries.DETAILS_ROUTE) {
            WeatherDetailsScreen(
                weatherLocation,
                onCloseClick = {
                    navController.navigateUp()
                }
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