package com.rodrigmatrix.weatheryou.presentation.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.presentation.utils.rememberWeatherYouAppState
import com.rodrigmatrix.weatheryou.presentation.utils.rememberWindowSizeClass

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherYouTheme {
                NavigationScreen(
                    appState = rememberWeatherYouAppState(),
                    windowSize = rememberWindowSizeClass()
                )
            }
        }
    }
}