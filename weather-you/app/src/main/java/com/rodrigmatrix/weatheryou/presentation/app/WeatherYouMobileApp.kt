package com.rodrigmatrix.weatheryou.presentation.app

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.updateAll
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import com.rodrigmatrix.weatheryou.components.theme.ColorMode
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import com.rodrigmatrix.weatheryou.components.theme.ThemeSettings
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.core.state.LocalWeatherYouAppSettings
import com.rodrigmatrix.weatheryou.core.state.WeatherYouAppState
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppSettingsUseCase
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeViewModel
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeNavigationRail
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeNavigationSuite
import com.rodrigmatrix.weatheryou.home.presentation.navigation.NavigationEntries
import com.rodrigmatrix.weatheryou.presentation.navigation.WeatherHomeNavHost
import com.rodrigmatrix.weatheryou.settings.utils.AppThemeManager
import com.rodrigmatrix.weatheryou.widgets.weather.CurrentWeatherWidget
import com.rodrigmatrix.weatheryou.widgets.weather.animated.CurrentAnimatedWeatherWidget
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun WeatherYouMobileApp(
    getAppSettingsUseCase: GetAppSettingsUseCase = koinInject<GetAppSettingsUseCase>(),
    appThemeManager: AppThemeManager = koinInject<AppThemeManager>(),
) {
    val defaultSettings = LocalWeatherYouAppSettings.current
    val context = LocalContext.current
    val activity = context as? Activity
    val intent = activity?.intent
    var colorMode by remember { mutableStateOf(ColorMode.Default) }
    var themeMode by remember { mutableStateOf(ThemeMode.Dark) }
    var appSettings by remember { mutableStateOf(defaultSettings) }
    val navController = rememberNavController()
    var currentDestination by remember {
        mutableStateOf(navController.currentDestination?.route.orEmpty())
    }
    val homeViewModel = koinViewModel<HomeViewModel>()
    val homeViewState by homeViewModel.viewState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val conditionsScaffoldState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val blurValue = if (conditionsScaffoldState.currentValue == SheetValue.Hidden) {
        0.dp
    } else {
        64.dp
    }
    LaunchedEffect(Unit) {
        val latitude = intent?.extras?.getDouble("latitude")
        val longitude = intent?.extras?.getDouble("longitude")
        if (latitude != null && longitude != null) {
            homeViewModel.openLocation(latitude = latitude, longitude = longitude)
        }
    }
    LaunchedEffect(Unit) {
        getAppSettingsUseCase().collect {
            colorMode = when (it.appColorPreference) {
                AppColorPreference.DYNAMIC -> ColorMode.Dynamic
                AppColorPreference.DEFAULT -> ColorMode.Default
                AppColorPreference.MOSQUE -> ColorMode.Mosque
                AppColorPreference.DARK_FERN -> ColorMode.DarkFern
                AppColorPreference.FRESH_EGGPLANT -> ColorMode.FreshEggplant
                AppColorPreference.CARMINE -> ColorMode.Carmine
                AppColorPreference.CINNAMON -> ColorMode.Cinnamon
                AppColorPreference.PERU_TAN -> ColorMode.PeruTan
                AppColorPreference.GIGAS -> ColorMode.Gigas
            }
            themeMode = when (it.appThemePreference) {
                AppThemePreference.SYSTEM_DEFAULT -> ThemeMode.Dark
                AppThemePreference.LIGHT -> ThemeMode.Light
                AppThemePreference.DARK -> ThemeMode.Dark
            }

            appThemeManager.setAppTheme(enableFollowSystem = false)
            appSettings = it
        }
    }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        currentDestination = destination.route.orEmpty()
    }
    val homeScreenNavigator = rememberListDetailPaneScaffoldNavigator<Int>(
        calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(currentWindowAdaptiveInfo())
    )
    Box(Modifier.blur(blurValue)) {
        WeatherYouAppState(
            appSettings = appSettings,
            currentDestination = currentDestination,
            conditionsScaffoldState = conditionsScaffoldState,
        ) {
            WeatherYouTheme(
                themeMode = themeMode,
                colorMode = colorMode,
                themeSettings = ThemeSettings(
                    showWeatherAnimations = appSettings.enableWeatherAnimations,
                    enableThemeColorForWeatherAnimations = appSettings.enableThemeColorWithWeatherAnimations,
                )
            ) {
                val adaptiveInfo = currentWindowAdaptiveInfo()
                val customNavSuiteType = with (adaptiveInfo) {
                    if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                        NavigationSuiteType.NavigationDrawer
                    } else {
                        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
                    }
                }
                val navSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
                NavigationSuiteScaffoldLayout(
                    layoutType = customNavSuiteType,
                    navigationSuite = {
                        if (navSuiteType == NavigationSuiteType.NavigationRail) {
                            HomeNavigationRail(
                                navController = navController,
                                currentDestination = currentDestination,
                                homeViewState = homeViewState,
                                appSettings = appSettings,
                            )
                        } else null
                    }
                ) {
                    Box {
                        WeatherHomeNavHost(
                            homeViewModel = homeViewModel,
                            homeViewState = homeViewState,
                            navController = navController,
                            onUpdateWidgets = {
                                coroutineScope.launch {
                                    CurrentWeatherWidget().updateAll(context)
                                    CurrentAnimatedWeatherWidget().updateAll(context)
                                }
                            },
                            homeScreenNavigator = homeScreenNavigator,
                        )
                        if (navSuiteType != NavigationSuiteType.NavigationRail) {
                            HomeNavigationSuite(
                                navController = navController,
                                currentDestination = currentDestination,
                                homeScreenNavigator = homeScreenNavigator,
                                onSearchClick = {
                                    navController.navigate(NavigationEntries.ADD_LOCATION_ROUTE)
                                },
                                modifier = Modifier.align(Alignment.BottomCenter),
                            )
                        }
                    }
                }
            }
        }
    }
}