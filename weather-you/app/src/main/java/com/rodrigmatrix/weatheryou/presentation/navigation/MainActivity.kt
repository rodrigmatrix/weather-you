package com.rodrigmatrix.weatheryou.presentation.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherYouTheme {
                val navController = rememberNavController()
                Surface(Modifier.fillMaxSize()) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        WeatherYouNavHost(navController)
                    }
                }
            }
        }
    }
}