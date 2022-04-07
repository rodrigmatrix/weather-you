package com.rodrigmatrix.weatheryou.presentation.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.presentation.utils.rememberWeatherYouAppState
import com.rodrigmatrix.weatheryou.presentation.utils.rememberWindowSizeClass

class MainActivity : AppCompatActivity() {

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