package com.rodrigmatrix.weatheryou.tv.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rodrigmatrix.weatheryou.addlocation.AddLocationScreen
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeScreen
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeEntry
import com.rodrigmatrix.weatheryou.home.presentation.navigation.NavigationEntries
import com.rodrigmatrix.weatheryou.presentation.about.AboutScreen
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherHomeNavHost(
    navController: NavHostController,
    onAddLocationClick: () -> Unit,
    isExpandedScreen: Boolean
) {
    val bottomBarState = remember {
        mutableStateOf(false)
    }
    NavHost(
        navController,
        startDestination = HomeEntry.Locations.route
    ) {
        composable(HomeEntry.Locations.route) {
            HomeScreen(
                bottomAppState = bottomBarState,
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
            AddLocationScreen(navController)
        }
    }
}