package com.rodrigmatrix.weatheryou.addlocation

import android.content.Context
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
import com.rodrigmatrix.weatheryou.core.compose.LaunchViewEffect
import com.rodrigmatrix.weatheryou.core.extensions.toast
import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.components.R as Strings
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import org.koin.androidx.compose.getViewModel

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

            is AddLocationViewEffect.ShowErrorString -> {
                context.toast(viewEffect.string)
            }
        }
    }
    AddLocationScreen(
        viewState,
        onQueryChanged = viewModel::onSearch,
        onLocationClick = viewModel::addLocation,
        onClearQuery = {
            if (viewState.searchText.isNotEmpty()) {
                viewModel.onSearch("")
            } else {
                keyboardController?.hide()
                navController.navigateUp()
            }
        },
        onFamousLocationClicked = viewModel::addFamousLocation
    )
}

@Composable
fun AddLocationScreen(
    viewState: AddLocationViewState,
    onQueryChanged: (String) -> Unit,
    onLocationClick: (SearchAutocompleteLocation?) -> Unit,
    onFamousLocationClicked: (City, Context) -> Unit,
    onClearQuery: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
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
                        onLocationClick(viewState.locationsList.firstOrNull())
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
                    text = stringResource(Strings.string.no_results_found),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 20.dp)
                )
            }
            LocationSuggestions(
                viewState.famousLocationsList,
                onLocationClick = {
                    onFamousLocationClicked(it, context)
                }
            )
        }
    }
}

@Composable
fun LocationSelectList(
    locationsList: List<SearchAutocompleteLocation>,
    onLocationClick: (SearchAutocompleteLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    LazyColumn(modifier, state = scrollState) {
        itemsIndexed(locationsList) { index, item ->
            LocationItem(item, index, scrollState, onLocationClick)
        }
    }
}

@Composable
fun LocationItem(
    location: SearchAutocompleteLocation,
    index: Int,
    scrollState: LazyListState,
    onLocationClick: (SearchAutocompleteLocation) -> Unit
) {
    Row(
        Modifier
            .padding(start = 32.dp, end = 32.dp, bottom = 10.dp)
            .fillMaxWidth()
            .clickable {
                onLocationClick(location)
            },
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = location.name,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(Strings.string.add_x_location, location),
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
            { _, _ -> },
            { },
        )
    }
}