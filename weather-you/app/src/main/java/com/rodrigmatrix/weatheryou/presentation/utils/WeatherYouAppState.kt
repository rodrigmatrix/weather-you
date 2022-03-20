package com.rodrigmatrix.weatheryou.presentation.utils

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeEntry
import com.rodrigmatrix.weatheryou.home.presentation.navigation.routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberWeatherYouAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController()
): WeatherYouAppState {
    return remember(scaffoldState, navController) {
        WeatherYouAppState(scaffoldState, navController, scaffoldState.snackbarHostState)
    }
}


@Stable
class WeatherYouAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    val snackbarState: SnackbarHostState
) {
    @Composable
    fun shouldShowBottomBar(): Boolean {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        return currentDestination?.route in HomeEntry.values().routes
    }
}