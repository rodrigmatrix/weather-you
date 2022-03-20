package com.rodrigmatrix.weatheryou.wearos.presentation.home

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour

@Composable
fun WeatherHour(weatherHour: WeatherHour) {
    Chip(
        label = { 
            Text(text = weatherHour.dateTime)
        },
        onClick = { /*TODO*/ }
    )
}