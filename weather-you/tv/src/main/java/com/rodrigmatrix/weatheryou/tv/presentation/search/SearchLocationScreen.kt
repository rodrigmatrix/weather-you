package com.rodrigmatrix.weatheryou.tv.presentation.search

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyListState
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.SearchBar
import com.rodrigmatrix.weatheryou.core.compose.LaunchViewEffect
import com.rodrigmatrix.weatheryou.core.extensions.toast
import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import org.koin.androidx.compose.getViewModel

@Composable
internal fun SearchLocationScreen(
    navController: NavController,
    viewModel: SearchLocationViewModel = getViewModel(),
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
            SearchLocationUiAction.LocationAdded -> {
                keyboardController?.hide()
                navController.navigateUp()
            }
            is SearchLocationUiAction.ShowError -> {
                context.toast(viewEffect.string)
            }

            is SearchLocationUiAction.ShowErrorString -> {
                context.toast(viewEffect.string)
            }
        }
    }
    SearchLocationScreen(
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
private fun SearchLocationScreen(
    uiState: SearchLocationUiState,
    onQueryChanged: (String) -> Unit,
    onLocationClick: (SearchAutocompleteLocation?) -> Unit,
    onFamousLocationClicked: (City, Context) -> Unit,
    onClearQuery: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    Column {
        SearchBar(
            query = uiState.searchText,
            onQueryChange = onQueryChanged,
            onSearchFocusChange = {
                if (it) {
                    focusRequester.requestFocus()
                }
            },
            onClearQuery = onClearQuery,
            searching = uiState.isLoading,
            modifier = Modifier.padding(bottom = 8.dp),
            keyboardActions = KeyboardActions(
                onDone = {
                    onLocationClick(uiState.locationsList.firstOrNull())
                }
            )
        )
        Column(Modifier) {
            if (uiState.isLocationsListVisible()) {
                LocationSelectList(
                    uiState.locationsList,
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
                uiState.famousLocationsList,
                onLocationClick = {
                    onFamousLocationClicked(it, context)
                }
            )
        }
    }
}

@Composable
private fun LocationSelectList(
    locationsList: List<SearchAutocompleteLocation>,
    onLocationClick: (SearchAutocompleteLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberTvLazyListState()
    TvLazyColumn(modifier, state = scrollState) {
        itemsIndexed(locationsList) { index, item ->
            LocationItem(item, index, scrollState, onLocationClick)
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun LocationItem(
    location: SearchAutocompleteLocation,
    index: Int,
    scrollState: TvLazyListState,
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
            contentDescription = stringResource(R.string.add_x_location, location),
            modifier = Modifier.size(34.dp)
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(device = Devices.TV_1080p)
@Composable
fun AddLocationScreenPreview() {
    MaterialTheme {
        SearchLocationScreen(
            uiState = SearchLocationUiState(
                famousLocationsList = PreviewFamousCities
            ),
            { },
            { },
            { _, _ -> },
            { },
        )
    }
}

val PreviewFamousCities = listOf(
    City(R.string.new_york, 0.0, 0.0, ""),
    City(R.string.los_angeles, 0.0, 0.0, ""),
    City(R.string.toronto, 0.0, 0.0, ""),
    City(R.string.vancouver, 0.0, 0.0, ""),
    City(R.string.rio_de_janeiro, 0.0, 0.0, ""),
    City(R.string.sao_paulo, 0.0, 0.0, "")
)