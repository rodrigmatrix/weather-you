package com.rodrigmatrix.weatheryou.wearos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rodrigmatrix.weatheryou.wearos.presentation.navigation.WeatherYouWearNavHost
import com.rodrigmatrix.weatheryou.wearos.theme.WeatherYouTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            WeatherYouTheme {
                WeatherYouWearNavHost()
            }
        }
    }
}