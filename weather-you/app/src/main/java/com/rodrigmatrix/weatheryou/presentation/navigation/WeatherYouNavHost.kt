@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rodrigmatrix.weatheryou.addlocation.AddLocationScreen
import com.rodrigmatrix.weatheryou.core.extensions.toast
import com.rodrigmatrix.weatheryou.presentation.about.AboutScreen
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeScreen
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeUiState
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeViewEffect
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeViewModel
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeEntry
import com.rodrigmatrix.weatheryou.home.presentation.navigation.NavigationEntries
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.WeatherHomeNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    homeViewState: HomeUiState,
    onUpdateWidgets: () -> Unit,
    homeScreenNavigator: ThreePaneScaffoldNavigator<Int>,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    NavHost(
        navController = navController,
        startDestination = HomeEntry.Locations.route,
        modifier = modifier,
    ) {
        composable(HomeEntry.Locations.route) {
            val context = LocalContext.current
            val onNavigateToLocation: (Int) -> Unit = { id ->
                coroutineScope.launch {
                    homeScreenNavigator.navigateTo(
                        pane = ListDetailPaneScaffoldRole.Detail,
                        contentKey = id,
                    )
                }
            }
            HomeScreen(
                navController = navController,
                homeUiState = homeViewState,
                navigator = homeScreenNavigator,
                onAddLocation = {
                    navController.navigate(NavigationEntries.ADD_LOCATION_ROUTE)
                },
                onPermissionGranted = homeViewModel::onLocationPermissionGranted,
                onDialogStateChanged = homeViewModel::onDialogStateChanged,
                onSwipeRefresh = homeViewModel::loadLocations,
                onLocationSelected = homeViewModel::selectLocation,
                onDeleteLocation = homeViewModel::deleteLocation,
                onDeleteLocationConfirmButtonClicked = homeViewModel::deleteLocation,
                onOrderChanged = homeViewModel::orderLocations,
                onNavigateToLocation = onNavigateToLocation,
                animatedVisibilityScope = this@composable,
                sharedTransitionScope = this@WeatherHomeNavHost,
            )
            LaunchedEffect(homeViewModel) {
                homeViewModel.viewEffect.collect { viewEffect ->
                    when (viewEffect) {
                        is HomeViewEffect.Error -> {
                            context.toast(viewEffect.stringRes)
                        }

                        HomeViewEffect.ShowInAppReview -> {

                        }

                        HomeViewEffect.UpdateWidgets -> {
                            onUpdateWidgets()
                        }

                        is HomeViewEffect.OpenLocation -> {
                            onNavigateToLocation(viewEffect.id)
                        }
                    }
                }
            }
        }
        composable(HomeEntry.Settings.route) {
            SettingsScreen(onFetchLocations = { })
        }
        composable(HomeEntry.About.route) {
            AboutScreen()
        }
        composable(NavigationEntries.ADD_LOCATION_ROUTE) {
            AddLocationScreen(navController)
        }
    }
}