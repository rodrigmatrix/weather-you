package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.content.res.Configuration
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.details.WindCardContent
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference

@Composable
fun WindCard(
    windSpeed: Double,
    windDirection: Double,
    unit: TemperaturePreference,
    modifier: Modifier = Modifier
) {
    WeatherYouCard(modifier) {
        WindCardContent(
            windSpeed = windSpeed,
            windDirection = windDirection,
            unit = unit,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WindCardPreview() {
    WeatherYouTheme {
        WindCard(
            windSpeed = 10.0,
            windDirection = 251.0,
            unit = TemperaturePreference.METRIC,
        )
    }
}