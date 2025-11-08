package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.content.res.Configuration
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.details.HumidityCardContent

@Composable
fun HumidityCard(
    humidity: Double,
    dewPoint: Double,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    WeatherYouCard(
        onClick = onClick,
        modifier = modifier
    ) {
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
    WeatherYouTheme {
        HumidityCard(
            humidity = 80.0,
            dewPoint = 22.0,
            onClick = {},
        )
    }
}