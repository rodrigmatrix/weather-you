package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.height
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.details.VisibilityCardContent
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference

@Composable
fun VisibilityCard(
    visibility: Double,
    modifier: Modifier = Modifier
) {
    WeatherYouCard(modifier) {
        VisibilityCardContent(
            visibility = visibility,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VisibilityCardPreview() {
    WeatherYouTheme {
        VisibilityCard(
            visibility = 80.0,
        )
    }
}