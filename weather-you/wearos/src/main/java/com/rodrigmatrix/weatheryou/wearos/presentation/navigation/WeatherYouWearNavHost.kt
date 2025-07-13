package com.rodrigmatrix.weatheryou.wearos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rodrigmatrix.weatheryou.wearos.presentation.editlocations.EditLocationsScreen
import com.rodrigmatrix.weatheryou.wearos.presentation.home.HomeScreen
import com.rodrigmatrix.weatheryou.wearos.presentation.location.AddLocationScreen
import com.rodrigmatrix.weatheryou.wearos.presentation.location.AddLocationViewModel
import com.rodrigmatrix.weatheryou.wearos.presentation.location.SearchLocationScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherYouWearNavHost(
    modifier: Modifier = Modifier,
    particleTick: Long,
    navController: NavHostController = rememberSwipeDismissableNavController(),
    addLocationViewModel: AddLocationViewModel = koinViewModel<AddLocationViewModel>(),
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = WeatherYouNavigation.Home.route,
        modifier = modifier,
    ) {
        composable(WeatherYouNavigation.Home.route) {
            HomeScreen(
                navController = navController,
                particleTick = particleTick,
            )
        }
        composable(WeatherYouNavigation.AddLocation.route) {
            AddLocationScreen(
                viewModel = addLocationViewModel,
                navController = navController,
            )
        }
        composable(WeatherYouNavigation.SearchLocation.route) {
            SearchLocationScreen(
                viewModel = addLocationViewModel,
                navController = navController,
            )
        }
        composable(WeatherYouNavigation.EditLocations.route) {
            EditLocationsScreen(
                particleTick = particleTick,
            )
        }

        composable(WeatherYouNavigation.Settings.route) {
            EditLocationsScreen(
                particleTick = particleTick,
            )
        }
    }
}