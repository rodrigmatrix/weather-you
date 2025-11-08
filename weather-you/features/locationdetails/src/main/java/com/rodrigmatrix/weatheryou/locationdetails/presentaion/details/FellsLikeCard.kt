package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.height
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.details.FeelsLikeCardContent
import com.rodrigmatrix.weatheryou.components.details.VisibilityCardContent
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference

@Composable
fun FeelsLikeCard(
    actual: Double,
    feelsLike: Double,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WeatherYouCard(
        onClick = onClick,
        modifier = modifier
    ) {
        FeelsLikeCardContent(
            actualTemp = actual,
            feelsLikeTemp = feelsLike,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FeelsLikeCardPreview() {
    WeatherYouTheme {
        FeelsLikeCard(
            actual = 16.0,
            feelsLike = 13.0,
            onClick = {},
        )
    }
}