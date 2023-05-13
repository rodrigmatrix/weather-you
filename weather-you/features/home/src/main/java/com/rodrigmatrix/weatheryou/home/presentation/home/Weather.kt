package com.rodrigmatrix.weatheryou.home.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.rodrigmatrix.weatheryou.core.extensions.getTimeZoneHourAndMinutes
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.home.presentation.preview.PreviewWeatherList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherLocationList(
    weatherLocationList: List<WeatherLocation>,
    selectedLocation: WeatherLocation?,
    onItemClick: (WeatherLocation) -> Unit,
    onDismiss: (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxHeight(),
    ) {
        items(
            items = weatherLocationList,
            key = { it.id },
        ) { item ->
            WeatherLocation(
                weatherLocation = item,
                isSelected = selectedLocation == item,
                onItemClick = onItemClick,
                onDismiss = onDismiss,
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}

@Composable
fun WeatherLocation(
    weatherLocation: WeatherLocation,
    isSelected: Boolean,
    onItemClick: (WeatherLocation) -> Unit,
    onDismiss: (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    WeatherYouCard(
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.secondaryContainer
        },
        isDismissible = weatherLocation.isCurrentLocation.not(),
        onClick = {
            onItemClick(weatherLocation)
        },
        onDismiss = {
            onDismiss(weatherLocation)
        },
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .fillMaxHeight()
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
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .height(100.dp),
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
                        tint = MaterialTheme.colorScheme.primary,
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
        WeatherLocationList(
            weatherLocationList = PreviewWeatherList,
            selectedLocation = null,
            onItemClick = { },
            onDismiss = { },
        )
    }
}