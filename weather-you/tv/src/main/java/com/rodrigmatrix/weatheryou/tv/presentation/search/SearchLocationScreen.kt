package com.rodrigmatrix.weatheryou.tv.presentation.search

import android.app.Activity
import android.content.pm.ApplicationInfo
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rodrigmatrix.weatheryou.components.R as Strings
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import com.rodrigmatrix.weatheryou.addlocation.AddLocationViewEffect
import com.rodrigmatrix.weatheryou.addlocation.AddLocationViewModel
import com.rodrigmatrix.weatheryou.addlocation.AddLocationViewState
import com.rodrigmatrix.weatheryou.addlocation.LocationSelectList
import com.rodrigmatrix.weatheryou.addlocation.LocationSuggestions
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.SearchBar
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.core.compose.LaunchViewEffect
import com.rodrigmatrix.weatheryou.core.extensions.toast
import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import com.rodrigmatrix.weatheryou.tv.components.TvCard
import org.koin.androidx.compose.getViewModel

@Composable
internal fun SearchLocationScreen(
    navController: NavController,
    viewModel: AddLocationViewModel = getViewModel(),
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
            AddLocationViewEffect.RequestInAppReview -> {
                runCatching {
                    val activity = context as? Activity
                    if (activity != null) {
                        val manager = if (0 != context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                            FakeReviewManager(activity)
                        } else {
                            ReviewManagerFactory.create(activity)
                        }
                        val request = manager.requestReviewFlow()
                        request.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val reviewInfo = task.result
                                manager.launchReviewFlow(activity, reviewInfo)
                            }
                        }
                    }
                }
            }
        }
    }
    SearchLocationScreen(
        viewState,
        onQueryChanged = viewModel::onQueryChanged,
        onLocationClick = { location ->
            viewModel.addLocation(location, context as Activity, showAds = false)
        },
        onClearQuery = {
            if (viewState.searchText.isNotEmpty()) {
                viewModel.onQueryChanged("")
            }
        },
        onFamousLocationClicked = { city ->
            viewModel.addFamousLocation(city, context as Activity, showAds = false)
        },
        onSearchButtonClicked = viewModel::search
    )
}

@Composable
private fun SearchLocationScreen(
    viewState: AddLocationViewState,
    onQueryChanged: (String) -> Unit,
    onSearchButtonClicked: () -> Unit,
    onLocationClick: (SearchAutocompleteLocation?) -> Unit,
    onFamousLocationClicked: (City) -> Unit,
    onClearQuery: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    Column {
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
            modifier = Modifier
                .padding(bottom = 8.dp)
                .focusRequester(focusRequester),
            showBackButton = false,
            onSearchButtonClicked = {
                onSearchButtonClicked()
            },
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchButtonClicked()
                }
            ),
        )
        Column(Modifier) {
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
private fun LocationSelectList(
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
private fun LocationItem(
    location: SearchAutocompleteLocation,
    index: Int,
    scrollState: LazyListState,
    onLocationClick: (SearchAutocompleteLocation) -> Unit
) {
    TvCard(
        onClick = {
            onLocationClick(location)
        },
        colors = CardDefaults.colors(
            containerColor = WeatherYouTheme.colorScheme.surface,
    ),
        modifier = Modifier
            .padding(start = 32.dp, end = 32.dp, bottom = 10.dp)
            .fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = location.name,
                    color = WeatherYouTheme.colorScheme.onSurface,
                    style = WeatherYouTheme.typography.headlineSmall,
                )
            }
            Icon(
                imageVector = Icons.Default.Add,
                tint = WeatherYouTheme.colorScheme.onSurface,
                contentDescription = stringResource(R.string.add_x_location, location),
                modifier = Modifier.size(34.dp)
            )
        }
    }
}

@Preview(device = Devices.TV_1080p)
@Composable
fun AddLocationScreenPreview() {
    WeatherYouTheme {
        SearchLocationScreen(
            viewState = AddLocationViewState(
                famousLocationsList = PreviewFamousCities
            ),
            { },
            { },
            { _ -> },
            { },
            { },
        )
    }
}

val PreviewFamousCities = listOf(
    City(R.string.new_york, 0.0, 0.0, "", "", ""),
    City(R.string.los_angeles, 0.0, 0.0, "", "", ""),
    City(R.string.toronto, 0.0, 0.0, "", "", ""),
    City(R.string.vancouver, 0.0, 0.0, "", "", ""),
    City(R.string.rio_de_janeiro, 0.0, 0.0, "", "", ""),
    City(R.string.sao_paulo, 0.0, 0.0, "", "", "")
)