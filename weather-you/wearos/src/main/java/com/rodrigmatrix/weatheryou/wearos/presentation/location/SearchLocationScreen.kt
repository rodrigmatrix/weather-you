package com.rodrigmatrix.weatheryou.wearos.presentation.location

import android.app.RemoteInput
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import com.rodrigmatrix.weatheryou.wearos.R

private const val SearchInputKey = "location_search_input"

@Composable
fun SearchLocationScreen(
    viewModel: AddLocationViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val remoteInputs: List<RemoteInput> = listOf(
        RemoteInput.Builder(SearchInputKey)
            .setLabel("Location")
            .wearableExtender {
                setEmojisAllowed(false)
                setInputActionType(EditorInfo.IME_ACTION_SEARCH)
            }.build(),
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.data == null && viewState.searchedLocations.isEmpty()) {
            navController.navigateUp()
        }
        it.data?.let { data ->
            val results: Bundle = RemoteInput.getResultsFromIntent(data)
            val newInputText: CharSequence? = results.getCharSequence(SearchInputKey)
            val input = newInputText?.toString().orEmpty()
            if (input.isEmpty()) {
                navController.navigateUp()
            } else {
                viewModel.onQueryChanged(input)
                viewModel.search()
            }
        }
    }
    val intent = RemoteInputIntentHelper.createActionRemoteInputIntent()
    RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)
    SearchLocationScreen(
        viewState = viewState,
        onSearchButtonClicked = {
            launcher.launch(intent)
        },
        onLocationClicked = {
            viewModel.addLocation(it)
            navController.navigateUp()
        },
        modifier = modifier,
    )
    LaunchedEffect(Unit) {
        launcher.launch(intent)
    }
}

@Composable
fun SearchLocationScreen(
    viewState: AddLocationViewState,
    onSearchButtonClicked: () -> Unit,
    onLocationClicked: (SearchAutocompleteLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScalingLazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            ListHeader {
                Button(
                    onClick = onSearchButtonClicked,
                    colors = ButtonDefaults.secondaryButtonColors(),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .size(
                            width = 60.dp,
                            height = 40.dp,
                        )
                        .padding(bottom = 10.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "Search",
                    )
                }
            }
        }
        if (viewState.isLoadingLocations) {
            item {
                CircularProgressIndicator(
                    trackColor = MaterialTheme.colors.primary,
                    modifier = Modifier.size(48.dp),
                )
            }
        } else {
            items(viewState.searchedLocations) { item ->
                LocationItem(
                    searchAutocompleteLocation = item,
                    onClick = onLocationClicked,
                )
            }
        }
    }
}

@Composable
fun LocationItem(
    searchAutocompleteLocation: SearchAutocompleteLocation,
    onClick: (SearchAutocompleteLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Chip(
        onClick = { onClick(searchAutocompleteLocation) },
        colors = ChipDefaults.secondaryChipColors(),
        label = {
            Text(
                text = searchAutocompleteLocation.name,
            )
        },
        modifier = modifier.fillMaxWidth(),
    )
}