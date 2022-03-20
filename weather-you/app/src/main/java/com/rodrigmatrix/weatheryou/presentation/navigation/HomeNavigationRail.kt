package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.components.extensions.dpadFocusable

@Composable
fun HomeNavigationRail(
    navController: NavController,
    onFabClick: () -> Unit,
    onNavigationItemClick: (HomeEntry) -> Unit,
    entries: Array<HomeEntry> = HomeEntry.values()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationRail(
        header = {
            FloatingActionButton(
                onClick = onFabClick,
                shape = CircleShape,
                modifier = Modifier.padding(8.dp).dpadFocusable()
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    modifier = Modifier.size(24.dp),
                    contentDescription = stringResource(R.string.add_location)
                )
            }
        }
    ) {
        entries.forEach { screen ->
            NavigationRailItem(
                icon = { Icon(painterResource(screen.icon), contentDescription = null) },
                label = { Text(stringResource(screen.stringRes)) },
                onClick = { onNavigationItemClick(screen) },
                selected = currentDestination?.route == screen.route,
                modifier = Modifier.dpadFocusable()
            )
        }
    }
}