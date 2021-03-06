package com.rodrigmatrix.weatheryou.tv.presentation.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.rodrigmatrix.weatheryou.components.WeatherYouScaffold
import com.rodrigmatrix.weatheryou.home.presentation.navigation.NavigationEntries
import com.rodrigmatrix.weatheryou.tv.presentation.theme.WeatherYouTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherYouTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val isExpandedScreen = true
    WeatherYouScaffold(
        modifier = Modifier.fillMaxSize(),
        navigationRail = {
            TvNavigationRail(
                navController,
                onNavigationItemClick = { screen ->
                    navController.navigate(screen.route) {
                        if (navController.currentDestination?.route == screen.route) {
                            return@navigate
                        }
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onFabClick = {
                    navController.navigate(NavigationEntries.ADD_LOCATION_ROUTE)
                }
            )
        },
        isExpandedScreen = isExpandedScreen
    ) {
        Surface {
            WeatherHomeNavHost(
                onAddLocationClick = {
                    navController.navigate(NavigationEntries.ADD_LOCATION_ROUTE)
                },
                isExpandedScreen = isExpandedScreen,
                navController = navController
            )
        }
    }
}