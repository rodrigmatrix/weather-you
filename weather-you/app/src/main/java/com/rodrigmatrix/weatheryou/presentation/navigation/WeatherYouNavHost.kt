@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherHomeNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    homeViewState: HomeUiState,
    onUpdateWidgets: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController,
        startDestination = HomeEntry.Locations.route,
        modifier = modifier,
    ) {
        composable(HomeEntry.Locations.route) {
            val context = LocalContext.current
            val navigator = rememberListDetailPaneScaffoldNavigator<Int>(
                calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(currentWindowAdaptiveInfo())
            )
            val onNavigateToLocation: (Int) -> Unit = { id ->
                navigator.navigateTo(
                    pane = ListDetailPaneScaffoldRole.Detail,
                    content = id,
                )
            }
            HomeScreen(
                navController = navController,
                homeUiState = homeViewState,
                navigator = navigator,
                onAddLocation = {
                    navController.navigate(NavigationEntries.ADD_LOCATION_ROUTE)
                },
                onPermissionGranted = homeViewModel::updateLocations,
                onDialogStateChanged = homeViewModel::onDialogStateChanged,
                onSwipeRefresh = homeViewModel::loadLocations,
                onLocationSelected = homeViewModel::selectLocation,
                onDeleteLocation = homeViewModel::deleteLocation,
                onDeleteLocationConfirmButtonClicked = homeViewModel::deleteLocation,
                onOrderChanged = homeViewModel::orderLocations,
                onNavigateToLocation = onNavigateToLocation,
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
            SettingsScreen(onFetchLocations = {  })
        }
        composable(HomeEntry.About.route) {
            AboutScreen()
        }
        composable(NavigationEntries.ADD_LOCATION_ROUTE) {
            AddLocationScreen(navController)
        }
    }
}