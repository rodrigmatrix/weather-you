package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.details.SunriseSunsetCardContent
import org.joda.time.DateTime

@Composable
fun SunriseSunsetCard(
    sunrise: DateTime,
    sunset: DateTime,
    currentTime: DateTime,
    modifier: Modifier = Modifier
) {
    WeatherYouCard(modifier) {
        SunriseSunsetCardContent(
            sunrise = sunrise,
            sunset = sunset,
            currentTime = currentTime,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SunriseSunsetCardPreview() {
    WeatherYouTheme {
        Column {
            SunriseSunsetCard(
                sunrise = DateTime(),
                sunset = DateTime(),
                currentTime = DateTime(),
            )
            SunriseSunsetCard(
                sunrise = DateTime(),
                sunset = DateTime(),
                currentTime = DateTime(),
            )
            SunriseSunsetCard(
                sunrise = DateTime(),
                sunset = DateTime(),
                currentTime = DateTime(),
            )
        }
    }
}