package com.rodrigmatrix.weatheryou.home.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.rodrigmatrix.weatheryou.components.extensions.getGradientList
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeUiState

@Composable
fun HomeNavigationRail(
    navController: NavController,
    currentDestination: String,
    homeViewState: HomeUiState,
    appSettings: AppSettings,
) {
    NavigationRail(
        containerColor = if (appSettings.enableWeatherAnimations && currentDestination == HomeEntry.Locations.route) {
            Color.Transparent
        } else {
            NavigationRailDefaults.ContainerColor
        },
        modifier = Modifier.background(
            if (appSettings.enableWeatherAnimations && currentDestination == HomeEntry.Locations.route) {
                Brush.verticalGradient(
                    homeViewState.getSelectedOrFirstLocation()?.getGradientList() ?: listOf(
                        NavigationRailDefaults.ContainerColor,
                        NavigationRailDefaults.ContainerColor
                    )
                )
            } else {
                SolidColor(Color.Transparent)
            }
        )
    ) {
        HomeEntry.entries.forEach { screen ->
            NavigationRailItem(
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