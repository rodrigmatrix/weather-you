package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.rodrigmatrix.weatheryou.components.WeatherYouScaffold
import com.rodrigmatrix.weatheryou.presentation.utils.WeatherYouAppState
import com.rodrigmatrix.weatheryou.presentation.utils.WindowSize

@Composable
fun NavigationScreen(
    appState: WeatherYouAppState,
    windowSize: WindowSize
) {
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val isExpandedScreen = windowSize == WindowSize.Expanded
    WeatherYouScaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (appState.shouldShowBottomBar() && bottomBarState.value) {
                HomeBottomBar(
                    appState.navController,
                    onNavigationItemClick = { screen ->
                        appState.navController.navigate(screen.route) {
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        },
        navigationRail = {
            HomeNavigationRail(
                appState.navController,
                onNavigationItemClick = { screen ->
                    appState.navController.navigate(screen.route) {
                        if (appState.navController.currentDestination?.route == screen.route) {
                            return@navigate
                        }
                        popUpTo(appState.navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onFabClick = {
                    appState.navController.navigate(NavigationEntries.ADD_LOCATION_ROUTE)
                }
            )
        },
        isExpandedScreen = isExpandedScreen
    ) {
        Surface {
            WeatherHomeNavHost(
                appState = appState,
                bottomAppState = bottomBarState,
                onAddLocationClick = {
                    appState.navController.navigate(NavigationEntries.ADD_LOCATION_ROUTE)
                },
                isExpandedScreen = isExpandedScreen
            )
        }
    }
}

