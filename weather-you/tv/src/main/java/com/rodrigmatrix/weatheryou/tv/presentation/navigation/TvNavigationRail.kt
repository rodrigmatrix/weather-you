package com.rodrigmatrix.weatheryou.tv.presentation.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rodrigmatrix.weatheryou.components.ExtendableFloatingActionButton
import com.rodrigmatrix.weatheryou.components.extensions.dpadFocusable
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeEntry
import com.rodrigmatrix.weatheryou.tv.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvNavigationRail(
    navController: NavController,
    onFabClick: () -> Unit,
    onNavigationItemClick: (HomeEntry) -> Unit,
    entries: Array<HomeEntry> = HomeEntry.values()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var selectedList by remember {
        mutableStateOf(listOf(false, false, false, false))
    }
    val fraction by animateFloatAsState(
        targetValue = if (selectedList.any { it }) 0.25f else 0.08f
    )
    NavigationRail(
        header = {
            ExtendableFloatingActionButton(
                onClick = onFabClick,
                extended = selectedList.any { it },
                text = {
                    Text(
                        text = stringResource(R.string.add_location),
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        modifier = Modifier.size(24.dp),
                        contentDescription = stringResource(R.string.add_location)
                    )
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        },
        modifier = Modifier.fillMaxWidth(fraction = fraction)
    ) {
        entries.forEachIndexed { index, screen ->
            NavigationDrawerItem(
                icon = { Icon(painterResource(screen.icon), contentDescription = null) },
                label = { Text(stringResource(screen.stringRes)) },
                onClick = { onNavigationItemClick(screen) },
                selected = currentDestination?.route == screen.route,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .dpadFocusable(
                        onFocusChanged = {
                            selectedList = selectedList.toMutableList().apply {
                                this[index + 1] = it
                            }
                        }
                    )
            )
        }
    }
}