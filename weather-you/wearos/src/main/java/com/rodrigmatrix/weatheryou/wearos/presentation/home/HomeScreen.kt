package com.rodrigmatrix.weatheryou.wearos.presentation.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.wearos.R
import com.rodrigmatrix.weatheryou.wearos.presentation.components.WeatherIcon
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = getViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    HomeScreen(viewState = viewState)
}

@Composable
fun HomeScreen(
    viewState: HomeViewState
) {
    when {
        viewState.isLoading -> {
            Loading()
        }
        viewState.error != null -> {
            Error(viewState.error)
        }
        viewState.weatherLocation != null -> {
            CurrentConditions(viewState.weatherLocation)
        }
    }
}

@Composable
private fun Loading() {
    Box(Modifier.fillMaxSize()) {

    }
}

@Composable
private fun Error(@StringRes error: Int) {

}

@Composable
fun CurrentConditions(
    weatherLocation: WeatherLocation
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp)
    ) {
        WeatherIcon(
            weatherIcons = weatherLocation.weatherIcons,
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.padding(bottom = 4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = weatherLocation.currentWeather.temperatureString(),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.display3
            )
            Spacer(Modifier.padding(end = 4.dp))
            Column {
                Text(
                    text = weatherLocation.currentWeatherDescription,
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.caption1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Fortaleza",
                    style = MaterialTheme.typography.caption2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(Modifier.padding(bottom = 4.dp))
        MaxAndLowestWeather(
            max = weatherLocation.maxTemperature,
            lowest = weatherLocation.lowestTemperature
        )
    }
}

@Composable
fun MaxAndLowestWeather(
    max: Double,
    lowest: Double,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_upward),
            contentDescription = null,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = max.temperatureString(),
            style = MaterialTheme.typography.caption2,
            color = MaterialTheme.colors.secondary
        )
        Spacer(Modifier.padding(end = 8.dp))
        Icon(
            painter = painterResource(R.drawable.ic_arrow_downward),
            contentDescription = null,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = lowest.temperatureString(),
            style = MaterialTheme.typography.caption2,
            color = MaterialTheme.colors.error
        )
    }
}