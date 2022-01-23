package com.rodrigmatrix.weatheryou.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.rodrigmatrix.weatheryou.domain.model.Hour
import com.rodrigmatrix.weatheryou.presentation.extensions.temperatureString
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewHourlyForecast

@Composable
fun HourlyForecast(
    hoursList: List<Hour>,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column {
            Text(
                text = "Daily Forecast",
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 10.dp,
                        top = 10.dp
                    ),
                style = MaterialTheme.typography.headlineMedium
            )
            LazyRow(Modifier.padding(start = 16.dp, end = 16.dp)) {
                items(hoursList) { item ->
                    HourRow(item)
                }
            }
        }
    }

}

@Composable
fun HourRow(hour: Hour) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(hour.icon))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Column(
        Modifier.padding(
            top = 10.dp,
            bottom = 10.dp,
            start = 16.dp,
            end = 16.dp
        )
    ) {
        Text(
            text = hour.temperature.temperatureString(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
                .size(42.dp)
        )
        Text(
            text = hour.time,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(name = "Hourly Forecast preview")
@Composable
fun WeatherLocationPreview() {
    WeatherYouTheme {
        HourlyForecast(PreviewHourlyForecast)
    }
}

