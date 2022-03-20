package com.rodrigmatrix.weatheryou.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.extensions.dpadFocusable
import com.rodrigmatrix.weatheryou.core.extensions.getTimeZoneCurrentTime
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.presentation.preview.PreviewWeatherList

@Composable
fun WeatherLocationList(
    weatherLocationList: List<com.rodrigmatrix.weatheryou.domain.model.WeatherLocation>,
    onItemClick: (com.rodrigmatrix.weatheryou.domain.model.WeatherLocation) -> Unit,
    onLongPress: (com.rodrigmatrix.weatheryou.domain.model.WeatherLocation) -> Unit,
    contentPaddingValues: PaddingValues = PaddingValues()
) {
    LazyColumn(
        contentPadding = contentPaddingValues,
        modifier = Modifier.fillMaxHeight()
    ) {
        items(weatherLocationList) { item ->
            WeatherLocation(item, onItemClick, onLongPress)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherLocation(
    weatherLocation: com.rodrigmatrix.weatheryou.domain.model.WeatherLocation,
    onItemClick: (com.rodrigmatrix.weatheryou.domain.model.WeatherLocation) -> Unit,
    onLongPress: (com.rodrigmatrix.weatheryou.domain.model.WeatherLocation) -> Unit
) {
    WeatherYouCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            )
            .dpadFocusable()
            .combinedClickable(
                onClick = {
                    onItemClick(weatherLocation)
                },
                onLongClick = {
                    onLongPress(weatherLocation)
                }
            )

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = weatherLocation.currentWeather.temperatureString(),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
                Text(
                    text = weatherLocation.timeZone.getTimeZoneCurrentTime().toString("hh:mm aa"),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
                Text(
                    text = weatherLocation.name,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
            WeatherIcon(
                weatherIcons = weatherLocation.weatherIcons,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp, end = 8.dp)
                    .size(64.dp)
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeatherLocationPreview() {
    MaterialTheme {
        WeatherLocationList(PreviewWeatherList, { }, {})
    }
}