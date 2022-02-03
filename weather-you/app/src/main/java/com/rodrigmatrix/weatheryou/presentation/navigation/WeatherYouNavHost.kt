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
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewWeatherLocation

@Composable
fun WeatherYouNavHost(navHostController: NavHostController) {
    var weather: WeatherLocation by remember {
        mutableStateOf(PreviewWeatherLocation)
    }
    NavHost(
        navHostController,
        startDestination = HomeEntries.Home.route
    ) {
        composable(HomeEntries.Home.route) {
            HomeScreen(
                onItemClick = {
                    weather = it
                    navHostController.navigate(NavigationEntries.DETAILS_ROUTE)
                },
                onAddLocation = {
                    navHostController.navigate(NavigationEntries.ADD_LOCATION_ROUTE)
                }
            )
        }
        composable(NavigationEntries.DETAILS_ROUTE) {
            WeatherDetailsScreen(weather)
        }
        composable(HomeEntries.Settings.route) {
            SettingsScreen()
        }
        composable(HomeEntries.About.route) {
            AboutScreen()
        }
        composable(NavigationEntries.ADD_LOCATION_ROUTE) {
            AddLocationScreen(navHostController)
        }
    }
}