package com.rodrigmatrix.weatheryou.presentation.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rodrigmatrix.weatheryou.presentation.app.WeatherYouMobileApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            WeatherYouMobileApp()
        }
    }
}
