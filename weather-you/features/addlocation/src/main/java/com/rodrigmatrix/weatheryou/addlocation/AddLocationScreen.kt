package com.rodrigmatrix.weatheryou.addlocation

import android.app.Activity
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rodrigmatrix.weatheryou.addlocation.preview.PreviewFamousCities
import com.rodrigmatrix.weatheryou.components.SearchBar
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
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
        onQueryChanged = viewModel::onQueryChanged,
        onLocationClick = { location ->
            viewModel.addLocation(location, context as Activity)
        },
        onClearQuery = {
            if (viewState.searchText.isNotEmpty()) {
                viewModel.onQueryChanged("")
            } else {
                keyboardController?.hide()
                navController.navigateUp()
            }
        },
        onFamousLocationClicked = { city ->
            viewModel.addFamousLocation(city, context as Activity)
        },
        onSearchButtonClicked = viewModel::search,
    )
}

@Composable
fun AddLocationScreen(
    viewState: AddLocationViewState,
    onQueryChanged: (String) -> Unit,
    onLocationClick: (SearchAutocompleteLocation?) -> Unit,
    onFamousLocationClicked: (City) -> Unit,
    onClearQuery: () -> Unit,
    onSearchButtonClicked: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    Scaffold(
        topBar = {
            SearchBar(
                query = viewState.searchText,
                onQueryChange = onQueryChanged,
                onSearchFocusChange = {
                    if (it) {
                        focusRequester.requestFocus()
                    }
                },
                onClearQuery = onClearQuery,
                searching = viewState.isLoading,
                onSearchButtonClicked = onSearchButtonClicked,
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchButtonClicked()
                    }
                ),
                modifier = Modifier
                    .padding(bottom = 8.dp),
            )
        },
        containerColor = WeatherYouTheme.colorScheme.background,
        modifier = Modifier.statusBarsPadding()
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            if (viewState.showKeepTyping) {
                Text(
                    text = stringResource(Strings.string.keep_typing),
                    color = WeatherYouTheme.colorScheme.onBackground,
                    style = WeatherYouTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 20.dp)
                )
            }
            if (viewState.showClickToSearch && viewState.isLoading.not()) {
                Text(
                    text = stringResource(Strings.string.click_to_search),
                    color = WeatherYouTheme.colorScheme.onBackground,
                    style = WeatherYouTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 20.dp)
                )
            }
            if (viewState.showEmptyState) {
                Text(
                    text = stringResource(Strings.string.no_results_found),
                    color = WeatherYouTheme.colorScheme.onBackground,
                    style = WeatherYouTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 20.dp)
                )
            }
            if (viewState.locationsList.isNotEmpty()) {
                LocationSelectList(
                    viewState.locationsList,
                    onLocationClick,
                    Modifier.focusRequester(focusRequester)
                )
            }
            LocationSuggestions(
                viewState.famousLocationsList,
                onLocationClick = onFamousLocationClicked,
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
                color = WeatherYouTheme.colorScheme.onBackground,
                style = WeatherYouTheme.typography.headlineSmall
            )
        }
        Icon(
            imageVector = Icons.Default.Add,
            tint = WeatherYouTheme.colorScheme.onBackground,
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
    WeatherYouTheme {
        AddLocationScreen(
            viewState = AddLocationViewState(
                famousLocationsList = PreviewFamousCities
            ),
            { },
            { },
            { _ -> },
            { },
            {},
        )
    }
}