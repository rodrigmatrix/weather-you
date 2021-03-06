package com.rodrigmatrix.weatheryou.home.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.extensions.dpadFocusable
import com.rodrigmatrix.weatheryou.core.extensions.getTimeZoneHourAndMinutes
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.home.presentation.preview.PreviewWeatherList

@Composable
fun WeatherLocationList(
    weatherLocationList: List<WeatherLocation>,
    onItemClick: (WeatherLocation) -> Unit,
    contentPaddingValues: PaddingValues = PaddingValues()
) {
    LazyColumn(
        contentPadding = contentPaddingValues,
        modifier = Modifier.fillMaxHeight()
    ) {
        items(weatherLocationList) { item ->
            WeatherLocation(item, onItemClick)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherLocation(
    weatherLocation: WeatherLocation,
    onItemClick: (WeatherLocation) -> Unit
) {
    val context = LocalContext.current
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
            .clickable { onItemClick(weatherLocation) }
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
                    text = weatherLocation.timeZone.getTimeZoneHourAndMinutes(context),
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxHeight()
            ) {
                WeatherIcon(
                    weatherIcons = weatherLocation.weatherIcons,
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center)
                )
                if (weatherLocation.isCurrentLocation) {
                    Icon(
                        painter = painterResource(R.drawable.ic_my_location),
                        contentDescription = stringResource(com.rodrigmatrix.weatheryou.locationdetails.R.string.current_location),
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeatherLocationPreview() {
    MaterialTheme {
        WeatherLocationList(PreviewWeatherList, { })
    }
}