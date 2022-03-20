package com.rodrigmatrix.weatheryou.wearos.presentation.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.rodrigmatrix.weatheryou.core.extensions.getHourWithMinutesString
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.wearos.R
import com.rodrigmatrix.weatheryou.wearos.presentation.components.CurvedText
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = getViewModel(),
    locationPermissionState: PermissionState = rememberPermissionState(ACCESS_COARSE_LOCATION)
) {
    val viewState by viewModel.viewState.collectAsState()
    HomeScreen(
        viewState = viewState,
        locationPermissionState = locationPermissionState,
        onRefreshLocation = viewModel::loadLocation
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewState: HomeViewState,
    locationPermissionState: PermissionState,
    onRefreshLocation: () -> Unit
) {
    when {
        locationPermissionState.hasPermission.not() -> {
            RequestLocationPermission(locationPermissionState, onRefreshLocation)
            return
        }
        viewState.isLoading -> {
            Loading()
            return
        }
        viewState.error != null -> {
            Error(
                error = viewState.error,
                onRefreshLocation = onRefreshLocation
            )
            return
        }
        viewState.weatherLocation != null -> {
            WeatherContent(viewState.weatherLocation)
        }
    }
}

@Composable
private fun Loading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Error(
    @StringRes error: Int,
    onRefreshLocation: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(error),
                style = MaterialTheme.typography.title3,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.padding(bottom = 4.dp))
            Button(
                modifier = Modifier.size(ButtonDefaults.SmallButtonSize),
                onClick = onRefreshLocation
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = stringResource(R.string.try_again)
                )
            }
        }
    }
}

@Composable
fun WeatherContent(
    weatherLocation: WeatherLocation
) {
    Scaffold(
        timeText = {
            CurvedText(
                text = weatherLocation.currentTime.getHourWithMinutesString(),
                style = MaterialTheme.typography.caption2
            )
        }
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.padding(bottom = 10.dp))
            }
            item {
                CurrentConditions(weatherLocation)
            }
            items(weatherLocation.hours) {
                WeatherHour(it)
            }
        }
    }
}