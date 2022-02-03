package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rodrigmatrix.weatheryou.core.extensions.shouldShowBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (navController.shouldShowBottomBar(HomeEntries.values().routes)) {
                HomeBottomBar(navController)
            }
        }
    ) {
        WeatherYouNavHost(navController)
    }
}

