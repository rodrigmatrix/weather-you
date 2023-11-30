package com.rodrigmatrix.weatheryou.tv.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rodrigmatrix.weatheryou.tv.presentation.home.WeatherLocationsScreen
import com.rodrigmatrix.weatheryou.tv.presentation.search.SearchLocationScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherYouTvNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = WeatherYouTvRoutes.HOME_SCREEN,
        modifier = modifier,
    ) {

        composable(WeatherYouTvRoutes.HOME_SCREEN) {
            WeatherLocationsScreen(
                navController = navController
            )
        }

        composable(WeatherYouTvRoutes.ADD_LOCATION_SCREEN) {
            SearchLocationScreen(navController = navController)
        }
    }
}