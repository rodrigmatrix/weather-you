package com.rodrigmatrix.weatheryou.presentation.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewHourlyForecast

@Composable
fun DetailsScreen() {
    Box(Modifier.fillMaxSize()) {
        HourlyForecast(
            PreviewHourlyForecast,
            Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            )
        )
    }
}