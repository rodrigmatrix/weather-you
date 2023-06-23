package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.details.WindCardContent

@Composable
fun WindCard(
    windSpeed: Double,
    windDirection: Double,
    modifier: Modifier = Modifier
) {
    WeatherYouCard(modifier) {
        WindCardContent(
            windSpeed = windSpeed,
            windDirection = windDirection,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WindCardPreview() {
    MaterialTheme {
        WindCard(
            windSpeed = 10.0,
            windDirection = 251.0
        )
    }
}