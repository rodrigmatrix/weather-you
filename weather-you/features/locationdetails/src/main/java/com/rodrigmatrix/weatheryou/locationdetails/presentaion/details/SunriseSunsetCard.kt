package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.details.SunriseSunsetCardContent

@Composable
fun SunriseSunsetCard(
    sunriseHour: Long,
    sunsetHour: Long,
    currentTime: Long,
    modifier: Modifier = Modifier
) {
    WeatherYouCard(modifier) {
        SunriseSunsetCardContent(
            sunriseHour = sunriseHour,
            sunsetHour = sunsetHour,
            currentTime = currentTime,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SunriseSunsetCardPreview() {
    MaterialTheme {
        Column {
            SunriseSunsetCard(
                sunriseHour = 0,
                sunsetHour = 0,
                currentTime = 0
            )
            SunriseSunsetCard(
                sunriseHour = 0,
                sunsetHour = 0,
                currentTime = 0
            )
            SunriseSunsetCard(
                sunriseHour = 0,
                sunsetHour = 0,
                currentTime = 0
            )
        }
    }
}