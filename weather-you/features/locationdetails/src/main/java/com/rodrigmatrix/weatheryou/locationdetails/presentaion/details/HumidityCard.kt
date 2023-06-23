package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.details.HumidityCardContent

@Composable
fun HumidityCard(
    humidity: Double,
    dewPoint: Double,
    modifier: Modifier = Modifier
) {
    WeatherYouCard(modifier) {
        HumidityCardContent(
            humidity = humidity,
            dewPoint = dewPoint,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HumidityCardPreview() {
    MaterialTheme {
        HumidityCard(
            humidity = 80.0,
            dewPoint = 22.0
        )
    }
}