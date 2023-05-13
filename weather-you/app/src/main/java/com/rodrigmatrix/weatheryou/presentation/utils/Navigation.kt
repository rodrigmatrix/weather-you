package com.rodrigmatrix.weatheryou.presentation.utils

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeEntry

class ScreenNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: HomeEntry) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}
