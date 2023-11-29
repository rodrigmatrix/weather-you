package com.rodrigmatrix.weatheryou.tv.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Border
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.ToggleableSurfaceDefaults
import com.rodrigmatrix.weatheryou.components.WeatherLocationCardContent
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.tv.presentation.details.WeatherLocationScreen

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    var location = remember { PreviewWeatherList.first() }
    Row(modifier.padding(vertical = 16.dp)) {
        WeatherLocationsList(
            weatherLocationsList = PreviewWeatherList,
            currentLocation = location,
            onWeatherLocationClicked = {
                location = it
            },
            modifier = Modifier.weight(1f)
        )
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.weight(1f)
        ) {
            WeatherLocationScreen(
                weatherLocation = location,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun WeatherLocationsList(
    weatherLocationsList: List<WeatherLocation>,
    currentLocation: WeatherLocation,
    onWeatherLocationClicked: (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = CircleShape.copy(CornerSize(20.dp))
    TvLazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        items(weatherLocationsList) { weatherLocation ->
            Surface(
                checked = currentLocation == weatherLocation,
                shape = ToggleableSurfaceDefaults.shape(cardShape),
                onCheckedChange = {
                    onWeatherLocationClicked(weatherLocation)
                },
                color = ToggleableSurfaceDefaults.color(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    selectedColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedColor = MaterialTheme.colorScheme.primaryContainer,
                    pressedColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                border = ToggleableSurfaceDefaults.border(
                    focusedBorder = Border(
                        border = BorderStroke(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        shape = cardShape,
                    )
                ),
                scale = ToggleableSurfaceDefaults.scale(
                    scale = 1f,
                    focusedScale = 1.02f,
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                WeatherLocationCardContent(weatherLocation = weatherLocation)
            }
        }
    }
}

