package com.rodrigmatrix.weatheryou.presentation.tv.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.rodrigmatrix.weatheryou.presentation.navigation.WeatherYouNavHost
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTv() {
    WeatherYouTheme(darkTheme = true) {
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) {
            WeatherYouNavHost(navController)
        }
    }
}