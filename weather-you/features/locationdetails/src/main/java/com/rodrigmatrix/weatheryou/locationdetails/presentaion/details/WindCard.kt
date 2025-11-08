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
    windGustSpeed: Double,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    WeatherYouCard(
        onClick = onClick,
        modifier = modifier,
    ) {
        WindCardContent(
            windSpeed = windSpeed,
            windDirection = windDirection,
            windGustSpeed = windGustSpeed,
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
            windGustSpeed = 15.0,
            onClick = {}
        )
    }
}