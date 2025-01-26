package com.rodrigmatrix.weatheryou.wearos.presentation.editlocations

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.rodrigmatrix.weatheryou.components.particle.WeatherAnimationsBackground
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherList
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.wearos.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.dialog.Alert
import org.koin.androidx.compose.getViewModel

@Composable
fun EditLocationsScreen(
    modifier: Modifier = Modifier,
    viewModel: EditLocationsViewModel = getViewModel(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    EditLocationsScreen(
        viewState = viewState,
        onDeleteClicked = viewModel::showDeleteLocation,
        modifier = modifier,
    )
    if (viewState.deletingLocation != null) {
        Alert(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = null,
                )
            },
            title = {
                Text(text = "Are you sure you want to delete this location?")
            },
            positiveButton = {
                Button(onClick = viewModel::deleteLocation) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = "Confirm",
                    )
                }
            },
            negativeButton = {
                Button(
                    onClick = {
                        viewModel.showDeleteLocation(null)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "Cancel",
                    )
                }
            }
        )
    }
}

@Composable
fun EditLocationsScreen(
    viewState: EditLocationsViewState,
    onDeleteClicked: (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScalingLazyColumn(
        modifier = modifier,
    ) {
        item {
            ListHeader {
                Text("Manage Locations")
            }
        }
        if (viewState.weatherLocations.isEmpty()) {
            item {
                Text("No Locations Found")
            }
        } else {
            items(viewState.weatherLocations) { item ->
                EditLocationItem(
                    weatherLocation = item,
                    onDeleteClicked = onDeleteClicked,
                )
            }
        }
    }
}

@Composable
fun EditLocationItem(
    weatherLocation: WeatherLocation,
    onDeleteClicked: (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.surface,
                ),
                shape = CircleShape,
            )
            .height(50.dp)
    ) {
        WeatherAnimationsBackground(
            weatherLocation = weatherLocation,
            modifier = Modifier.clip(CircleShape),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        ) {
            Icon(
                painter = painterResource(com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_reorder),
                contentDescription = null,
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = weatherLocation.name,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            if (!weatherLocation.isCurrentLocation) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onDeleteClicked(weatherLocation)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditLocationsScreenPreview() {
    WeatherYouTheme {
        EditLocationsScreen(
            viewState = EditLocationsViewState(
                weatherLocations = PreviewWeatherList
            ),
            onDeleteClicked = { },
        )
    }
}