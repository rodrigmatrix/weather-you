package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rodrigmatrix.weatheryou.components.ScreenNavigationContentPosition
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
        containerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        Column(
            modifier = Modifier.layoutId(LayoutType.CONTENT),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            HomeEntry.values().forEach { screen ->
                NavigationRailItem(
                    icon = { Icon(painterResource(screen.icon), contentDescription = null) },
                    label = { Text(stringResource(screen.stringRes)) },
                    onClick = { onNavigationItemClick(screen) },
                    selected = currentDestination?.route == screen.route,
                )
            }
        }
    }
}

enum class LayoutType {
    HEADER, CONTENT
}