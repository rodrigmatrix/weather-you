package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rodrigmatrix.weatheryou.presentation.details.DetailsScreen
import com.rodrigmatrix.weatheryou.presentation.home.HomeScreen

@Composable
fun WeatherYouNavHost(navHostController: NavHostController) {
    NavHost(
        navHostController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onItemClick = {
                    navHostController.navigate(Screen.Details.route)
                }
            )
        }
        composable(Screen.Details.route) {
            DetailsScreen()
        }
    }
}