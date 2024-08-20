package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.content.res.Configuration
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.details.CurrentWeatherContent
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherLocation
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

@Composable
fun CurrentWeather(
    weatherLocation: WeatherLocation,
    modifier: Modifier = Modifier
) {
    WeatherYouCard(modifier = modifier) {
        CurrentWeatherContent(weatherLocation = weatherLocation)
    }
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CurrentWeatherPreview() {
    WeatherYouTheme {
        CurrentWeather(
            weatherLocation = PreviewWeatherLocation
        )
    }
}