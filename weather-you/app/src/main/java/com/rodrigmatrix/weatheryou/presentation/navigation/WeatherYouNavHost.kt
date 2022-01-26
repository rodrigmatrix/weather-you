package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.details.DetailsScreen
import com.rodrigmatrix.weatheryou.presentation.home.HomeScreen
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewWeatherList
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
            DetailsScreen(weather)
        }
    }
}