package com.rodrigmatrix.weatheryou.presentation.widget

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme

class CurrentWeatherWidgetConfigurationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherYouTheme {
                CurrentWeatherWidgetConfigurationScreen()
            }
        }
    }
}