package com.rodrigmatrix.weatheryou.presentation.home

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewWeatherList
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(
    onItemClick: (WeatherLocation) -> Unit,
    viewModel: HomeViewModel = getViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    HomeScreen(
        viewState = viewState,
        onItemClick = onItemClick
    )
}

@Composable
fun HomeScreen(
    viewState: HomeViewState,
    onItemClick: (WeatherLocation) -> Unit
) {
    WeatherLocationList(
        viewState.locationsList,
        onItemClick
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(device = Devices.PIXEL_C, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    WeatherYouTheme {
        HomeScreen(
            viewState = HomeViewState(locationsList = PreviewWeatherList),
            {}
        )
    }
}