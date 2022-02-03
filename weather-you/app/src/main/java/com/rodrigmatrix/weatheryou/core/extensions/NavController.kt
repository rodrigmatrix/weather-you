package com.rodrigmatrix.weatheryou.core.extensions

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavController.shouldShowBottomBar(routes: List<String>): Boolean {
    return currentBackStackEntryAsState().value?.destination?.route in routes
}