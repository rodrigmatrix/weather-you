package com.rodrigmatrix.weatheryou.presentation.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewFutureDaysForecast
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewHourlyForecast
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewWeatherLocation
import org.koin.androidx.compose.getViewModel

@Composable
fun DetailsScreen(
    weatherLocation: WeatherLocation,
    viewModel: WeatherDetailsViewModel = getViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    viewModel.setWeatherLocation(weatherLocation)

    WeatherDetailsScreen(
        viewState = viewState,
        onExpandedButtonClick = {
            viewModel.onFutureWeatherButtonClick(it)
        }
    )
}

@Composable
fun WeatherDetailsScreen(
    viewState: WeatherDetailsViewState,
    onExpandedButtonClick: (Boolean) -> Unit
) {
    LazyColumn {
        item {
            viewState.weatherLocation?.let {
                CurrentWeather(
                    it,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    )
                )
            }
        }
        item {
            HourlyForecast(
                hoursList = viewState.todayWeatherHoursList,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 10.dp,
                    bottom = 10.dp
                )
            )
        }
        item {
            FutureDaysForecast(
                futureDaysList = viewState.futureDaysList,
                isExpanded = viewState.isFutureWeatherExpanded,
                onExpandedButtonClick = onExpandedButtonClick,
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    )
            )
        }
    }
}