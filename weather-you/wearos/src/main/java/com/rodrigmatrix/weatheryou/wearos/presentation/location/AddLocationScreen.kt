package com.rodrigmatrix.weatheryou.wearos.presentation.location

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import coil.compose.AsyncImage
import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.wearos.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.wearos.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.dialog.Confirmation
import com.rodrigmatrix.weatheryou.wearos.presentation.components.ErrorDialog

@Composable
fun AddLocationScreen(
    viewModel: AddLocationViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    var errorTitle by remember { mutableStateOf("") }

    AddLocationScreen(
        viewState = viewState,
        onVoiceSearchClicked = {
            navController.navigate("search_location")
        },
        onTypeLocationClicked = {
            navController.navigate("search_location")
        },
        onAddFamousLocationClicked = {
            viewModel.addFamousLocation(it, context)
        },
        modifier = modifier,
    )
    if (errorTitle.isNotEmpty()) {
        ErrorDialog(
            error = errorTitle,
            onTimeout = {
                errorTitle = ""
            }
        )
    }
    LaunchedEffect(Unit) {
        viewModel.viewEffect.collect { viewEffect ->
            when (viewEffect) {
                AddLocationViewEffect.LocationAdded -> navController.navigateUp()
                is AddLocationViewEffect.ShowError -> {
                    errorTitle = context.getString(viewEffect.string)
                }
                is AddLocationViewEffect.ShowErrorString -> {
                    errorTitle = viewEffect.string
                }
            }
        }
    }
}

@Composable
fun AddLocationScreen(
    viewState: AddLocationViewState,
    onTypeLocationClicked: () -> Unit,
    onVoiceSearchClicked: () -> Unit,
    onAddFamousLocationClicked: (City) -> Unit,
    modifier: Modifier = Modifier,
) {
    val vignetteState = remember { mutableStateOf(VignettePosition.TopAndBottom) }
    Scaffold(
        positionIndicator = {

        },
        vignette = {
            Vignette(vignettePosition = vignetteState. value)
        },
        timeText = { TimeText() },
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            ListHeader {
                Text(
                    text = "Add Location",
                    style = MaterialTheme.typography.title3
                )
            }
            if (viewState.isAddingLocation) {
                CircularProgressIndicator(
                    trackColor = MaterialTheme.colors.primary,
                    modifier = Modifier.size(48.dp),
                )
            } else {
                AddLocationButtons(
                    onTypeLocationClicked = onTypeLocationClicked,
                    onVoiceSearchClicked = onVoiceSearchClicked,
                )
                FamousLocations(
                    famousLocations = viewState.famousLocations,
                    onClick = onAddFamousLocationClicked,
                )
            }
        }
    }
}

@Composable
fun AddLocationButtons(
    onTypeLocationClicked: () -> Unit,
    onVoiceSearchClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Button(onClick = onVoiceSearchClicked) {
            Icon(
                painter = painterResource(R.drawable.ic_mic),
                contentDescription = "Voice search location",
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onTypeLocationClicked) {
            Icon(
                painter = painterResource(R.drawable.ic_keyboard_alt),
                contentDescription = "Type search location",
            )
        }
    }
}

@Composable
fun FamousLocations(
    famousLocations: List<City>,
    onClick: (City) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScalingLazyColumn(modifier = modifier) {
        items(famousLocations) { item ->
            FamousLocationItem(
                city = item,
                onClick = onClick,
            )
        }
    }
}

@Composable
fun FamousLocationItem(
    city: City,
    onClick: (City) -> Unit,
    modifier: Modifier = Modifier,
) {
    Chip(
        onClick = { onClick(city) },
        colors = ChipDefaults.secondaryChipColors(),
        label = {
            Text(
                text = stringResource(city.name),
            )
        },
        icon = {
            AsyncImage(
                model = city.image,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(ChipDefaults.IconSize)
                    .clip(CircleShape),
            )
        },
        modifier = modifier.fillMaxWidth(),
    )
    
}

@Preview
@Composable
private fun AddLocationScreenPreview() {
    WeatherYouTheme {
        AddLocationScreen(
            viewState = AddLocationViewState(),
            onTypeLocationClicked = {},
            onVoiceSearchClicked = {},
            onAddFamousLocationClicked = {},
        )
    }
}