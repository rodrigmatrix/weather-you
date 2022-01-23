package com.rodrigmatrix.weatheryou.presentation.home

import androidx.compose.runtime.Composable
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewWeatherList

@Composable
fun HomeScreen(onItemClick: (WeatherLocation) -> Unit) {
    WeatherLocationList(PreviewWeatherList, onItemClick)
}