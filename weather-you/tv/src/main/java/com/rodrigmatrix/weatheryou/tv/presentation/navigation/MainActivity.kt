package com.rodrigmatrix.weatheryou.tv.presentation.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rodrigmatrix.weatheryou.tv.presentation.home.WeatherLocationsScreen
import com.rodrigmatrix.weatheryou.tv.presentation.theme.WeatherYouTheme

class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherYouTheme {
                WeatherYouTvNavHost()
            }
        }
    }
}