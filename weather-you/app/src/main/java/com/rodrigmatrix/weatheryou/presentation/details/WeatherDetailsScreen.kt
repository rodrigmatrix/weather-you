package com.rodrigmatrix.weatheryou.presentation.details

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.UiMode
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewFutureDaysForecast
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewHourlyForecast
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewWeatherLocation
import org.koin.androidx.compose.getViewModel

@Composable
fun WeatherDetailsScreen(
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
                    ).focusable()
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
                ).focusable()
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

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview(device = Devices.PIXEL_C)
@Preview(device = Devices.PIXEL_C, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun WeatherDetailsScreenPreview() {
    WeatherYouTheme {
        WeatherDetailsScreen(
            viewState = WeatherDetailsViewState(
                weatherLocation = PreviewWeatherLocation,
                todayWeatherHoursList = PreviewHourlyForecast,
                futureDaysList = PreviewFutureDaysForecast
            ),
            onExpandedButtonClick = { }
        )
    }
}