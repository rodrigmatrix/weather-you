package com.rodrigmatrix.weatheryou.wearos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rodrigmatrix.weatheryou.wearos.presentation.home.HomeScreen
import com.rodrigmatrix.weatheryou.wearos.presentation.location.AddLocationScreen
import com.rodrigmatrix.weatheryou.wearos.presentation.location.AddLocationViewModel
import com.rodrigmatrix.weatheryou.wearos.presentation.location.SearchLocationScreen
import com.rodrigmatrix.weatheryou.wearos.theme.WeatherYouTheme
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            WeatherYouTheme {
                val navController = rememberNavController()
                val addLocationViewModel = getViewModel<AddLocationViewModel>()
                NavHost(
                    navController = navController,
                    startDestination = "home",
                ) {
                    composable("home") {
                        HomeScreen(navController = navController)
                    }
                    composable("add_location") {
                        AddLocationScreen(
                            viewModel = addLocationViewModel,
                            navController = navController,
                        )
                    }
                    composable("search_location") {
                        SearchLocationScreen(
                            viewModel = addLocationViewModel,
                            navController = navController,
                        )
                    }
                }
            }
        }
    }
}