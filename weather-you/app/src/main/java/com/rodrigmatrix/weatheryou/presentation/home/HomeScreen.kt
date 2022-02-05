package com.rodrigmatrix.weatheryou.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
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
    showFab: Boolean,
    viewModel: HomeViewModel = getViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    HomeScreen(
        viewState = viewState,
        onItemClick = onItemClick,
        onSwipeRefresh = viewModel::loadLocations,
        onAddLocation = onAddLocation,
        showFab = showFab
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewState: HomeViewState,
    onItemClick: (WeatherLocation) -> Unit,
    onSwipeRefresh: () -> Unit,
    onAddLocation: () -> Unit,
    showFab: Boolean
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(viewState.isLoading),
        onRefresh = onSwipeRefresh,
        swipeEnabled = viewState.locationsList.isNotEmpty()
    ) {
        Scaffold(
            floatingActionButton = {
                if (showFab) {
                    LargeFloatingActionButton(
                        onClick = onAddLocation,
                        modifier = Modifier.padding(bottom = 80.dp),
                        shape = RoundedCornerShape(100)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            modifier = Modifier.size(24.dp),
                            contentDescription = stringResource(R.string.add_location)
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) {
            when {
                viewState.isLoading.not() && viewState.locationsList.isEmpty() -> {

                }
                else -> {
                    WeatherLocationList(
                        viewState.locationsList,
                        onItemClick,
                        onItemClick,
                        contentPaddingValues = PaddingValues(
                            bottom = if (showFab) 200.dp else 0.dp
                        )
                    )
                }
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
            { },
            showFab = false
        )
    }
}