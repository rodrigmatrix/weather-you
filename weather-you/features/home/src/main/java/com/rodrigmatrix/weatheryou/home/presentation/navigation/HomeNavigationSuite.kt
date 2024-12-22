package com.rodrigmatrix.weatheryou.home.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeUiState

@Composable
fun HomeNavigationSuite(
    navController: NavController,
    currentDestination: String,
    homeViewState: HomeUiState,
) {
    if (
        homeViewState.selectedWeatherLocation == null &&
        currentDestination != "add_location"
    ) {
        NavigationSuite {
            HomeEntry.entries.forEach { screen ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(screen.icon),
                            tint = WeatherYouTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(screen.stringRes),
                            color = WeatherYouTheme.colorScheme.onSurface,
                        )
                    },
                    selected = currentDestination == screen.route,
                    onClick = {
                        navController.navigate(screen.route)
                    }
                )
            }
        }
    }
}