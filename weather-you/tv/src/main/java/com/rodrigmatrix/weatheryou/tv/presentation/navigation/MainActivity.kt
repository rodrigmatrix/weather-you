package com.rodrigmatrix.weatheryou.tv.presentation.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.rodrigmatrix.weatheryou.tv.presentation.theme.WeatherYouTvTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherYouTvTheme {
                WeatherYouTvNavHost()
            }
        }
    }
}