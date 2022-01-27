package com.rodrigmatrix.weatheryou.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.extensions.temperatureString
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewWeatherList
import java.util.*

@Composable
fun WeatherLocationList(
    weatherLocationList: List<WeatherLocation>,
    onItemClick: (WeatherLocation) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(weatherLocationList) { item ->
            WeatherLocation(item, onItemClick)
        }
    }
}

@Composable
fun WeatherLocation(
    weatherLocation: WeatherLocation,
    onItemClick: (WeatherLocation) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            )
            .clickable {
                onItemClick(weatherLocation)
            }
    ) {
        val composition by rememberLottieComposition(RawRes(weatherLocation.currentWeatherIcon))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        ) {
            Column {
                Text(
                    text = weatherLocation.currentWeather.temperatureString(),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
                Text(
                    text = weatherLocation.currentTime,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
                Text(
                    text = weatherLocation.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
            LottieAnimation(
                composition,
                progress,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp, end = 16.dp)
                    .size(64.dp)
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeatherLocationPreview() {
    WeatherYouTheme {
        WeatherLocationList(PreviewWeatherList, { })
    }
}