package com.rodrigmatrix.weatheryou.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewWeatherList
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(
    onItemClick: (WeatherLocation) -> Unit,
    onAddLocation: () -> Unit,
    viewModel: HomeViewModel = getViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    HomeScreen(
        viewState = viewState,
        onItemClick = onItemClick,
        onSwipeRefresh = viewModel::loadLocations,
        onAddLocation = onAddLocation
    )
}

@Composable
fun HomeScreen(
    viewState: HomeViewState,
    onItemClick: (WeatherLocation) -> Unit,
    onSwipeRefresh: () -> Unit,
    onAddLocation: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(viewState.isLoading),
        onRefresh = onSwipeRefresh,
        swipeEnabled = viewState.locationsList.isNotEmpty()
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when {
                viewState.isLoading.not() && viewState.locationsList.isEmpty() -> {

                }

                else -> {
                    WeatherLocationList(
                        viewState.locationsList,
                        onItemClick,
                        onItemClick
                    )
                }
            }
            LargeFloatingActionButton(
                onClick = onAddLocation,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp),
                shape = RoundedCornerShape(100)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    modifier = Modifier.size(24.dp),
                    contentDescription = stringResource(R.string.add_location)
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(device = Devices.PIXEL_C)
@Preview(device = Devices.PIXEL_C, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    WeatherYouTheme {
        HomeScreen(
            viewState = HomeViewState(locationsList = PreviewWeatherList),
            { },
            { },
            { }
        )
    }
}