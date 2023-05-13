package com.rodrigmatrix.weatheryou.addlocation

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rodrigmatrix.weatheryou.addlocation.preview.PreviewFamousCities
import com.rodrigmatrix.weatheryou.components.extensions.dpadFocusable
import com.rodrigmatrix.weatheryou.core.compose.LaunchViewEffect
import com.rodrigmatrix.weatheryou.core.extensions.toast
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddLocationScreen(
    navController: NavController,
    viewModel: AddLocationViewModel = getViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    BackHandler {
        keyboardController?.hide()
        navController.navigateUp()
    }
    LaunchViewEffect(viewModel) { viewEffect ->
        when (viewEffect) {
            AddLocationViewEffect.LocationAdded -> {
                keyboardController?.hide()
                navController.navigateUp()
            }
            is AddLocationViewEffect.ShowError -> {
                context.toast(viewEffect.string)
            }
        }
    }
    AddLocationScreen(
        viewState,
        onQueryChanged = {
            viewModel.onSearch(it)
        },
        onLocationClick = {
            viewModel.addLocation(it)
        },
        onClearQuery = {
            if (viewState.searchText.isNotEmpty()) {
                viewModel.onSearch("")
            } else {
                keyboardController?.hide()
                navController.navigateUp()
            }
        }
    )
}

@Composable
fun AddLocationScreen(
    viewState: AddLocationViewState,
    onQueryChanged: (String) -> Unit,
    onLocationClick: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    Scaffold(
        topBar = {
            com.rodrigmatrix.weatheryou.components.SearchBar(
                query = viewState.searchText,
                onQueryChange = onQueryChanged,
                onSearchFocusChange = {
                    if (it) {
                        focusRequester.requestFocus()
                    }
                },
                onClearQuery = onClearQuery,
                searching = viewState.isLoading,
                modifier = Modifier.padding(bottom = 8.dp),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onLocationClick(viewState.locationsList.firstOrNull()?.placeId.orEmpty())
                    }
                )
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            if (viewState.isLocationsListVisible()) {
                LocationSelectList(
                    viewState.locationsList,
                    onLocationClick,
                    Modifier.focusRequester(focusRequester)
                )
            } else {
                Text(
                    text = stringResource(R.string.no_results_found),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 20.dp)
                )
            }
            LocationSuggestions(
                viewState.famousLocationsList,
                onLocationClick = {
                    onLocationClick(it.placeId)
                }
            )
        }
    }
}

@Composable
fun LocationSelectList(
    locationsList: List<SearchAutocompleteLocation>,
    onLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    LazyColumn(modifier, state = scrollState) {
        itemsIndexed(locationsList) { index, item ->
            LocationItem(item, index, scrollState, onLocationClick)
        }
        if (locationsList.isNotEmpty()) {
            item {
                Column(Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(R.drawable.powered_by_google),
                        contentDescription = stringResource(R.string.powered_by_google),
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                            .size(width = 130.dp, height = 50.dp)
                            .align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
fun LocationItem(
    location: SearchAutocompleteLocation,
    index: Int,
    scrollState: LazyListState,
    onLocationClick: (String) -> Unit
) {
    Row(
        Modifier
            .padding(start = 32.dp, end = 32.dp, bottom = 10.dp)
            .fillMaxWidth()
            .dpadFocusable(index, scrollState)
            .clickable {
                onLocationClick(location.placeId)
            },
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = location.locationName,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_x_location, location),
            modifier = Modifier.size(34.dp)
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(device = Devices.PIXEL_C)
@Preview(device = Devices.PIXEL_C, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddLocationScreenPreview() {
    MaterialTheme {
        AddLocationScreen(
            viewState = AddLocationViewState(
                famousLocationsList = PreviewFamousCities
            ),
            { },
            { },
            { }
        )
    }
}