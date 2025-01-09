package com.rodrigmatrix.weatheryou.xr

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.xr.compose.material3.EnableXrComponentOverrides
import androidx.xr.compose.material3.ExperimentalMaterial3XrApi
import androidx.xr.compose.platform.LocalHasXrSpatialFeature
import androidx.xr.compose.platform.LocalSession
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.spatial.EdgeOffset
import androidx.xr.compose.spatial.Orbiter
import androidx.xr.compose.spatial.OrbiterEdge
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.layout.SpatialRoundedCornerShape
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.movable
import androidx.xr.compose.subspace.layout.resizable
import androidx.xr.compose.subspace.layout.width
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rodrigmatrix.weatheryou.addlocation.AddLocationScreen
import com.rodrigmatrix.weatheryou.components.extensions.getGradientList
import com.rodrigmatrix.weatheryou.components.theme.ColorMode
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import com.rodrigmatrix.weatheryou.components.theme.ThemeSettings
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.core.extensions.toast
import com.rodrigmatrix.weatheryou.core.state.LocalWeatherYouAppSettings
import com.rodrigmatrix.weatheryou.core.state.WeatherYouAppState
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppSettingsUseCase
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeScreen
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeUiState
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeViewEffect
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeViewModel
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeEntry
import com.rodrigmatrix.weatheryou.home.presentation.navigation.HomeNavigationSuite
import com.rodrigmatrix.weatheryou.home.presentation.navigation.NavigationEntries
import com.rodrigmatrix.weatheryou.presentation.about.AboutScreen
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsScreen
import com.rodrigmatrix.weatheryou.settings.utils.AppThemeManager
import com.rodrigmatrix.weatheryou.xr.ui.theme.WeatherYouTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3XrApi::class)
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WeatherYouXrApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3XrApi::class)
@SuppressLint("RestrictedApi")
@Composable
fun MySpatialContent(
    blurValue: Dp,
    themeMode: ThemeMode,
    colorMode: ColorMode,
    appSettings: AppSettings,
    homeViewModel: HomeViewModel,
    homeViewState: HomeUiState,
    currentDestination: String,
    conditionsScaffoldState: SheetState,
    navController: NavHostController,
    onRequestHomeSpaceMode: () -> Unit,
) {
    EnableXrComponentOverrides {
        Subspace {
            SpatialPanel(SubspaceModifier.width(1280.dp).height(800.dp).resizable().movable()) {
                WeatherHomeNavHost(
                    homeViewModel = homeViewModel,
                    homeViewState = homeViewState,
                    navController = navController,
                    onUpdateWidgets = { },
                )
                Orbiter(
                    position = OrbiterEdge.Start,
                    offset = EdgeOffset.inner(offset = 20.dp),
                    shape = SpatialRoundedCornerShape(CornerSize(28.dp))
                ) {
                    HomeNavigationRail(
                        navController = navController,
                        currentDestination = currentDestination,
                        homeViewState = homeViewState,
                        appSettings = appSettings,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3XrApi::class)
@Composable
fun WeatherYouXrApp(
    getAppSettingsUseCase: GetAppSettingsUseCase = koinInject<GetAppSettingsUseCase>(),
    appThemeManager: AppThemeManager = koinInject<AppThemeManager>(),
) {
    val session = LocalSession.current
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
    val homeViewModel = getViewModel<HomeViewModel>()
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

    if (LocalSpatialCapabilities.current.isSpatialUiEnabled) {
        MySpatialContent(
            blurValue = blurValue,
            themeMode = themeMode,
            colorMode = colorMode,
            appSettings = appSettings,
            homeViewModel = homeViewModel,
            homeViewState = homeViewState,
            currentDestination = currentDestination,
            conditionsScaffoldState = conditionsScaffoldState,
            navController = navController,
            onRequestHomeSpaceMode = {
                session?.requestHomeSpaceMode()
            }
        )
    } else {
        Xr2dContent(
            blurValue = blurValue,
            themeMode = themeMode,
            colorMode = colorMode,
            appSettings = appSettings,
            homeViewModel = homeViewModel,
            homeViewState = homeViewState,
            currentDestination = currentDestination,
            conditionsScaffoldState = conditionsScaffoldState,
            navController = navController,
        )
    }
    LaunchedEffect(Unit) {
        session?.requestFullSpaceMode()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Xr2dContent(
    blurValue: Dp,
    themeMode: ThemeMode,
    colorMode: ColorMode,
    appSettings: AppSettings,
    homeViewModel: HomeViewModel,
    homeViewState: HomeUiState,
    currentDestination: String,
    conditionsScaffoldState: SheetState,
    navController: NavHostController,
) {
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
                        } else {
                            HomeNavigationSuite(
                                navController = navController,
                                currentDestination = currentDestination,
                                homeViewState = homeViewState,
                            )
                        }
                    }
                ) {
                    WeatherHomeNavHost(
                        homeViewModel = homeViewModel,
                        homeViewState = homeViewState,
                        navController = navController,
                        onUpdateWidgets = { },
                    )
                }
            }
        }
    }
}

@Composable
fun HomeBottomBar(
    navController: NavController,
    entries: Array<HomeEntry> = HomeEntry.entries.toTypedArray(),
    onNavigationItemClick: (HomeEntry) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        entries.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(painterResource(screen.icon), contentDescription = null) },
                label = { Text(stringResource(screen.stringRes)) },
                onClick = { onNavigationItemClick(screen) },
                selected = currentDestination?.route == screen.route
            )
        }
    }
}


@OptIn(ExperimentalMaterial3XrApi::class)
@Composable
fun HomeNavigationRail(
    navController: NavController,
    currentDestination: String,
    homeViewState: HomeUiState,
    appSettings: AppSettings,
) {
    NavigationRail(
        containerColor = if (appSettings.enableWeatherAnimations && currentDestination == HomeEntry.Locations.route) {
            Color.Transparent
        } else {
            NavigationRailDefaults.ContainerColor
        },
        modifier = Modifier.background(
            if (appSettings.enableWeatherAnimations && currentDestination == HomeEntry.Locations.route) {
                Brush.verticalGradient(
                    homeViewState.getSelectedOrFirstLocation()?.getGradientList() ?: listOf(
                        NavigationRailDefaults.ContainerColor,
                        NavigationRailDefaults.ContainerColor
                    )
                )
            } else {
                SolidColor(Color.Transparent)
            }
        )
    ) {
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
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalPermissionsApi::class)
@Composable
fun WeatherHomeNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    homeViewState: HomeUiState,
    onUpdateWidgets: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
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
                coroutineScope.launch {
                    navigator.navigateTo(
                        pane = ListDetailPaneScaffoldRole.Detail,
                        contentKey = id,
                    )
                }
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

enum class LayoutType {
    HEADER, CONTENT
}