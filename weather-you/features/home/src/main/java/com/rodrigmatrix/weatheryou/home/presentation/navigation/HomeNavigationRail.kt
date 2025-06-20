package com.rodrigmatrix.weatheryou.home.presentation.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumExtendedFloatingActionButton
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.SmallExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRail
import androidx.compose.material3.WideNavigationRailDefaults
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rodrigmatrix.weatheryou.components.extensions.getGradientList
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeUiState
import kotlinx.coroutines.launch
import com.rodrigmatrix.weatheryou.components.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeNavigationRail(
    navController: NavController,
    currentDestination: String,
    homeViewState: HomeUiState,
    appSettings: AppSettings,
) {
    val state = rememberWideNavigationRailState()
    val scope = rememberCoroutineScope()
    Crossfade(
        if (appSettings.enableWeatherAnimations && currentDestination == HomeEntry.Locations.route) {
            Color.Transparent
        } else {
            NavigationRailDefaults.ContainerColor
        },
        animationSpec = tween(1200),
    ) { color ->
        WideNavigationRail(
            header = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (state.targetValue == WideNavigationRailValue.Expanded)
                                    state.collapse()
                                else state.expand()
                            }
                        },
                        modifier = Modifier
                            .semantics {
                                stateDescription =
                                    if (state.currentValue == WideNavigationRailValue.Expanded) {
                                        "Expanded"
                                    } else {
                                        "Collapsed"
                                    }
                            },
                    ) {
                        if (state.targetValue == WideNavigationRailValue.Expanded) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                                tint = WeatherYouTheme.colorScheme.onSurface,
                                contentDescription = "Collapse rail",
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                tint = WeatherYouTheme.colorScheme.onSurface,
                                contentDescription = "Expand rail",
                            )
                        }
                    }
                    SmallExtendedFloatingActionButton(
                        text = {
                            Text(
                                text = stringResource(R.string.search_location),
                                color = WeatherYouTheme.colorScheme.onSurface,
                                style = WeatherYouTheme.typography.labelLarge,
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.search_location)
                            )
                        },
                        expanded = state.targetValue == WideNavigationRailValue.Expanded,
                        onClick = {
                            navController.navigate(NavigationEntries.ADD_LOCATION_ROUTE)
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            },
            colors = WideNavigationRailDefaults.colors(
                containerColor = color,
            ),
            state = state,
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
            ),
        ) {
            HomeEntry.entries.forEach { screen ->
                WideNavigationRailItem(
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
                    railExpanded = state.targetValue == WideNavigationRailValue.Expanded,
                    selected = currentDestination == screen.route,
                    onClick = {
                        navController.navigate(screen.route)
                    }
                )
            }
        }
    }
}