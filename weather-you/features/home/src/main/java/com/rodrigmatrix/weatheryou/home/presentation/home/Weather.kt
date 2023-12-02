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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.WeatherLocationCardContent
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
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
        WeatherLocationCardContent(
            weatherLocation = weatherLocation,
        )
    }
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeatherLocationPreview() {
    WeatherYouTheme {
        WeatherLocationList(
            weatherLocationList = PreviewWeatherList,
            selectedLocation = null,
            onItemClick = { },
            onDismiss = { },
        )
    }
}