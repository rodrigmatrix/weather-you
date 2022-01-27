package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.details.WeatherDetailsScreen
import com.rodrigmatrix.weatheryou.presentation.home.HomeScreen
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewWeatherLocation

@Composable
fun WeatherYouNavHost(navHostController: NavHostController) {
    var weather: WeatherLocation by remember {
        mutableStateOf(PreviewWeatherLocation)
    }
    NavHost(
        navHostController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                onItemClick = {
                    weather = it
                    navHostController.navigate(Screen.Details.route)
                }
            )
        }
        composable(Screen.Details.route) {
            WeatherDetailsScreen(weather)
        }
    }
}