package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rodrigmatrix.weatheryou.components.ScreenNavigationContentPosition
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeEntry

@Composable
fun HomeNavigationRail(
    navController: NavController,
    navigationContentPosition: ScreenNavigationContentPosition,
    onNavigationItemClick: (HomeEntry) -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = WeatherYouTheme.colorScheme.inverseOnSurface,
    ) {
        Column(
            modifier = Modifier.layoutId(LayoutType.CONTENT),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            HomeEntry.entries.forEach { screen ->
                NavigationRailItem(
                    icon = { Icon(painterResource(screen.icon), contentDescription = null) },
                    label = { Text(stringResource(screen.stringRes)) },
                    onClick = { onNavigationItemClick(screen) },
                    selected = currentDestination?.route == screen.route,
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = WeatherYouTheme.colorScheme.onSecondaryContainer,
                        selectedTextColor = WeatherYouTheme.colorScheme.onSecondaryContainer,
                        indicatorColor = WeatherYouTheme.colorScheme.secondaryContainer,
                        unselectedIconColor = WeatherYouTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = WeatherYouTheme.colorScheme.onSurfaceVariant,
                    ),
                )
            }
        }
    }
}

enum class LayoutType {
    HEADER, CONTENT
}