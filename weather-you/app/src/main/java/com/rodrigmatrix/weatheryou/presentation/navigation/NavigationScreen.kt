package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rodrigmatrix.weatheryou.core.extensions.shouldShowBottomBar
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.components.WeatherYouScaffold
import com.rodrigmatrix.weatheryou.presentation.details.WeatherDetailsScreen
import com.rodrigmatrix.weatheryou.presentation.utils.WindowSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(
    navController: NavHostController = rememberNavController(),
    windowSize: WindowSize
) {
    val isExpandedScreen = windowSize == WindowSize.Expanded
    var showDetails by remember {
        mutableStateOf(false)
    }
    var weatherLocation: WeatherLocation? by remember {
        mutableStateOf(null)
    }
    WeatherYouScaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (navController.shouldShowBottomBar(HomeEntry.values().routes)) {
                HomeBottomBar(
                    navController,
                    onNavigationItemClick = { screen ->
                        showDetails = false
                        navController.navigate(screen.route) {
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        },
        navigationRail = {
            HomeNavigationRail(
                navController,
                onNavigationItemClick = { screen ->
                    showDetails = false
                    navController.navigate(screen.route) {
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onFabClick = {
                    showDetails = false
                    navController.navigate(NavigationEntries.ADD_LOCATION_ROUTE)
                }
            )
        },
        detailContent = {
            WeatherDetailsScreen(
                weatherLocation = weatherLocation,
                onCloseClick = {
                    showDetails = false
                }
            )
        },
        isExpandedScreen = isExpandedScreen,
        detailContentVisible = showDetails
    ) {
        Surface {
            WeatherHomeNavHost(
                navController = navController,
                onWeatherLocationClick = { newLocation ->
                    if (isExpandedScreen) {
                        showDetails = showDetails.not() || newLocation != weatherLocation
                        weatherLocation = newLocation
                    } else {
                        weatherLocation = newLocation
                        navController.navigate(NavigationEntries.DETAILS_ROUTE)
                    }
                },
                onAddLocationClick = {
                    navController.navigate(NavigationEntries.ADD_LOCATION_ROUTE)
                },
                isExpandedScreen = isExpandedScreen,
                weatherLocation = weatherLocation
            )
        }
    }
}

