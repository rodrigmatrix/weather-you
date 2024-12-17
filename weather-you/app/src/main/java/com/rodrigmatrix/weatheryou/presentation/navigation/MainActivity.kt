package com.rodrigmatrix.weatheryou.presentation.navigation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.glance.appwidget.updateAll
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import com.rodrigmatrix.weatheryou.components.theme.ColorMode
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import com.rodrigmatrix.weatheryou.components.theme.ThemeSettings
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.core.state.LocalWeatherYouAppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppSettingsUseCase
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeEntry
import com.rodrigmatrix.weatheryou.settings.utils.AppThemeManager
import com.rodrigmatrix.weatheryou.core.state.WeatherYouAppState
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeViewModel
import com.rodrigmatrix.weatheryou.widgets.weather.CurrentWeatherWidget
import com.rodrigmatrix.weatheryou.widgets.weather.animated.CurrentAnimatedWeatherWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.getViewModel

class MainActivity : AppCompatActivity() {

    private val getAppSettingsUseCase by inject<GetAppSettingsUseCase>()
    private val appThemeManager: AppThemeManager by inject()

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val defaultSettings = LocalWeatherYouAppSettings.current
            var colorMode by remember { mutableStateOf(ColorMode.Default) }
            var themeMode by remember { mutableStateOf(ThemeMode.Dark) }
            var appSettings by remember { mutableStateOf(defaultSettings) }
            val navController = rememberNavController()
            var currentDestination by remember {
                mutableStateOf(navController.currentDestination?.route.orEmpty())
            }
            val homeViewModel = getViewModel<HomeViewModel>()
            val homeViewState by homeViewModel.viewState.collectAsState()
            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                val latitude = intent.extras?.getDouble("latitude")
                val longitude = intent.extras?.getDouble("longitude")
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
            Box {
                WeatherYouAppState(
                    appSettings = appSettings,
                    currentDestination = currentDestination,
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
                                    NavigationRail {
                                        HomeEntry.entries.forEach { screen ->
                                            NavigationRailItem(
                                                icon = {
                                                    Icon(
                                                        painter = painterResource(screen.icon),
                                                        tint = WeatherYouTheme.colorScheme.onSurface,
                                                        contentDescription = null
                                                    )
                                                },
                                                label = {
                                                    Text(
                                                        text = stringResource(screen.stringRes),
                                                        color = WeatherYouTheme.colorScheme.onSurface,
                                                    )
                                                },
                                                selected = currentDestination == screen.route,
                                                onClick = {
                                                    navController.navigate(screen.route)
                                                }
                                            )
                                        }
                                    }
                                } else {
                                    if (
                                        homeViewState.selectedWeatherLocation == null &&
                                        currentDestination != "add_location"
                                    ) {
                                        NavigationSuite {
                                            HomeEntry.entries.forEach { screen ->
                                                item(
                                                    icon = {
                                                        Icon(
                                                            painter = painterResource(screen.icon),
                                                            tint = WeatherYouTheme.colorScheme.onSurface,
                                                            contentDescription = null
                                                        )
                                                    },
                                                    label = {
                                                        Text(
                                                            text = stringResource(screen.stringRes),
                                                            color = WeatherYouTheme.colorScheme.onSurface,
                                                        )
                                                    },
                                                    selected = currentDestination == screen.route,
                                                    onClick = {
                                                        navController.navigate(screen.route)
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        ) {
                            WeatherHomeNavHost(
                                homeViewModel = homeViewModel,
                                homeViewState = homeViewState,
                                navController = navController,
                                onUpdateWidgets = {
                                    coroutineScope.launch {
                                        CurrentWeatherWidget().updateAll(applicationContext)
                                        CurrentAnimatedWeatherWidget().updateAll(applicationContext)
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
