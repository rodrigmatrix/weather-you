package com.rodrigmatrix.weatheryou.presentation.addLocation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.core.compose.LaunchViewEffect
import com.rodrigmatrix.weatheryou.presentation.components.SearchBar
import org.koin.androidx.compose.getViewModel

@Composable
fun AddLocationScreen(
    navController: NavController,
    viewModel: AddLocationViewModel = getViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    LaunchViewEffect(viewModel) { viewEffect ->
        when (viewEffect) {
            AddLocationViewEffect.LocationAdded -> {
                navController.navigateUp()
            }
            is AddLocationViewEffect.ShowError -> {
                viewEffect.string
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
                navController.navigateUp()
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddLocationScreen(
    viewState: AddLocationViewState,
    onQueryChanged: (String) -> Unit,
    onLocationClick: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        SearchBar(
            query = viewState.searchText,
            onQueryChange  = onQueryChanged,
            onSearchFocusChange = {

            },
            onClearQuery = onClearQuery,
            searching = viewState.isLoading,
            Modifier.padding(bottom = 40.dp)
        )
        if (viewState.locationsList.isNotEmpty()) {
            Text(
                text = stringResource(R.string.click_to_add),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 10.dp)
            )
        }
        LocationSelectList(
            viewState.locationsList,
            onLocationClick,
            Modifier.focusable()
        )
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
    Text(
        text = location,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 10.dp)
            .focusable()
            .clickable {
                onLocationClick(location)
            }
    )
}