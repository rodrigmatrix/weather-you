package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.details.PressureCardContent
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.model.PressureTrend

@Composable
fun PressureCard(
    pressure: Double,
    trend: PressureTrend,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WeatherYouCard(
        onClick = onClick,
        modifier = modifier
    ) {
        PressureCardContent(
            pressure = pressure,
            trend = trend,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PressureCardPreview() {
    WeatherYouTheme {
        PressureCard(
            pressure = 1000.0,
            trend = PressureTrend.Steady,
            onClick = {},
        )
    }
}