package com.rodrigmatrix.weatheryou.presentation.addLocation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.core.compose.LaunchViewEffect
import com.rodrigmatrix.weatheryou.core.extensions.toast
import com.rodrigmatrix.weatheryou.presentation.components.SearchBar
import com.rodrigmatrix.weatheryou.presentation.utils.WeatherYouAppState
import org.koin.androidx.compose.getViewModel

@Composable
fun AddLocationScreen(
    appState: WeatherYouAppState,
    viewModel: AddLocationViewModel = getViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    val context = LocalContext.current
    LaunchViewEffect(viewModel) { viewEffect ->
        when (viewEffect) {
            AddLocationViewEffect.LocationAdded -> {
                appState.navController.navigateUp()
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
                appState.navController.navigateUp()
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddLocationScreen(
    viewState: AddLocationViewState,
    onQueryChanged: (String) -> Unit,
    onLocationClick: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    Scaffold(
        topBar = {
            SearchBar(
                query = viewState.searchText,
                onQueryChange  = onQueryChanged,
                onSearchFocusChange = {

                },
                onClearQuery = onClearQuery,
                searching = viewState.isLoading,
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .focusable(),
                keyboardActions = KeyboardActions(
                    onDone = { onLocationClick(viewState.searchText) }
                )
            )
        }
    ) {
        Column {
            LocationSelectList(
                viewState.locationsList,
                onLocationClick,
                Modifier.focusable()
            )
            LocationSuggestions(
                viewState.famousLocationsList,
                onLocationClick = {
                    onLocationClick(it.fullName)
                }
            )
        }
    }
}

@Composable
fun LocationSelectList(
    locationsList: List<String>,
    onLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(locationsList) { item ->
            LocationItem(item, onLocationClick)
        }
    }
}

@Composable
fun LocationItem(
    location: String,
    onLocationClick: (String) -> Unit
) {
    Row(
        Modifier
            .padding(start = 32.dp, end = 32.dp, bottom = 10.dp)
            .fillMaxWidth()
            .focusable()
            .clickable {
                onLocationClick(location)
            },
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = location,
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