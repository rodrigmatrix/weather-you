package com.rodrigmatrix.weatheryou.home.presentation.home

import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarExitDirection
import androidx.compose.material3.FloatingToolbarScrollBehavior
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.PaneExpansionAnchor
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.extensions.getGradientList
import com.rodrigmatrix.weatheryou.components.location.RequestBackgroundLocationDialog
import com.rodrigmatrix.weatheryou.components.particle.WeatherAnimationsBackground
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherList
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.details.WeatherDetailsScreen
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalPermissionsApi::class, ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3AdaptiveApi::class
)
@Composable
fun HomeScreen(
    navController: NavController,
    homeUiState: HomeUiState,
    navigator: ThreePaneScaffoldNavigator<Int>,
    onLocationSelected: (WeatherLocation?) -> Unit,
    onDialogStateChanged: (HomeDialogState) -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onDeleteLocationConfirmButtonClicked: () -> Unit,
    onAddLocation: () -> Unit,
    onPermissionGranted: () -> Unit,
    onOrderChanged: (List<WeatherLocation>) -> Unit,
    backgroundLocationPermissionState: PermissionState = rememberPermissionState(
        permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        } else {
            android.Manifest.permission.ACCESS_FINE_LOCATION
        },
        onPermissionResult = {
            onPermissionGranted()
        }
    ),
    locationPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        onPermissionsResult = {
            if (it.all { it.value }) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    onPermissionGranted()
                    onDialogStateChanged(HomeDialogState.BackgroundLocation)
                } else {
                    onPermissionGranted()
                }
            } else {
                onPermissionGranted()
            }
        }
    ),
    onNavigateToLocation: (Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val pagerState = rememberPagerState {
        homeUiState.locationsList.size
    }

    LaunchedEffect(pagerState, homeUiState.locationsList) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page < homeUiState.locationsList.size) {
                onLocationSelected(homeUiState.locationsList.getOrNull(page))
            }
        }
    }

    LaunchedEffect(homeUiState.locationsList) {
        if (homeUiState.locationsList.isEmpty() && navigator.canNavigateBack()) {
            navigator.navigateBack()
        }
    }

    BackHandler(enabled = homeUiState.isLocationSelected()) {
        onLocationSelected(null)
    }

    when (homeUiState.dialogState) {
        HomeDialogState.BackgroundLocation -> {
            RequestBackgroundLocationDialog(
                onRequestPermissionClicked = {
                    backgroundLocationPermissionState.launchPermissionRequest()
                    onDialogStateChanged(HomeDialogState.Hidden)
                },
                onDismissRequest = {
                    onPermissionGranted()
                    onDialogStateChanged(HomeDialogState.Hidden)
                },
            )
        }
        HomeDialogState.DeleteLocation -> {
            DeleteLocationDialog(
                onDismiss = {
                    onDialogStateChanged(HomeDialogState.Hidden)
                },
                onDeleteLocationClicked = onDeleteLocationConfirmButtonClicked,
            )
        }
        HomeDialogState.Hidden -> Unit
    }

    HomeScreen(
        homeUiState = homeUiState,
        pagerState = pagerState,
        navigator = navigator,
        showLocationPermissionRequest = homeUiState.showLocationPermissionRequest(locationPermissionState),
        onLocationSelected = onLocationSelected,
        onSwipeRefresh = onSwipeRefresh,
        onDeleteLocation = onDeleteLocation,
        onDeleteLocationClicked = {
            onDialogStateChanged(HomeDialogState.DeleteLocation)
        },
        onAddLocation = onAddLocation,
        onRequestPermission = locationPermissionState::launchMultiplePermissionRequest,
        onOrderChanged = onOrderChanged,
        onNavigateToLocation = onNavigateToLocation,
        animatedVisibilityScope = animatedVisibilityScope,
        sharedTransitionScope = sharedTransitionScope,
    )
}

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3AdaptiveApi::class
)
@Composable
fun WeatherLocationsListScreen(
    uiState: HomeUiState,
    particleTick: Long,
    navigator: ThreePaneScaffoldNavigator<Int>,
    showLocationPermissionRequest: Boolean,
    onItemClick: (WeatherLocation) -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onSearchLocationClick: () -> Unit,
    onRequestPermission: () -> Unit,
    onOrderChanged: (List<WeatherLocation>) -> Unit,
    onNavigateToLocation: (Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        state = pullToRefreshState,
        isRefreshing = uiState.isLoading,
        onRefresh = onSwipeRefresh,
        indicator = {
            PullToRefreshDefaults.LoadingIndicator(
                isRefreshing = uiState.isLoading,
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        },
        modifier = modifier,
    ) {
        when {
            showLocationPermissionRequest -> {
                RequestLocationPermission(
                    onRequestPermission = onRequestPermission,
                )
            }

            uiState.isLoading.not() && uiState.locationsList.isEmpty() -> {
                WeatherLocationsEmptyState(
                    Modifier
                )
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    WeatherLocationList(
                        weatherLocationList = uiState.locationsList,
                        particleTick = particleTick,
                        isRefreshingLocations = uiState.isRefreshingLocations,
                        selectedLocation = uiState.selectedWeatherLocation,
                        onItemClick = onItemClick,
                        onDismiss = onDeleteLocation,
                        onOrderChanged = onOrderChanged,
                        animatedVisibilityScope = animatedVisibilityScope,
                        sharedTransitionScope = sharedTransitionScope,
                        modifier = Modifier,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class, ExperimentalSharedTransitionApi::class
)
@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    pagerState: PagerState,
    showLocationPermissionRequest: Boolean,
    navigator: ThreePaneScaffoldNavigator<Int>,
    onLocationSelected: (WeatherLocation?) -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onDeleteLocationClicked: () -> Unit,
    onAddLocation: () -> Unit,
    onRequestPermission: () -> Unit,
    onOrderChanged: (List<WeatherLocation>) -> Unit,
    onNavigateToLocation: (Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val coroutineScope = rememberCoroutineScope()
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val paneExpansionState = rememberPaneExpansionState(
        keyProvider = navigator.scaffoldValue,
        anchors = listOf(
            PaneExpansionAnchor.Proportion(0.35f),
            PaneExpansionAnchor.Proportion(0.55f),
        ),
    )
    var showDragHandle by remember { mutableStateOf(true) }
    val paneInteractionSource = remember { MutableInteractionSource() }
    val isPaneDragging by paneInteractionSource.collectIsDraggedAsState()
    val navSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)

    BackHandler(navigator.canNavigateBack()) {
        coroutineScope.launch {
            navigator.navigateBack()
            onLocationSelected(null)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "particleTick")
    val particleTick by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1_000_000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 100_000,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "particleTick",
    )
    val exitAlwaysScrollBehavior = FloatingToolbarDefaults.exitAlwaysScrollBehavior(
        exitDirection = FloatingToolbarExitDirection.Bottom,
        snapAnimationSpec = spring(),
    )
    ListDetailPaneScaffold(
        value = navigator.scaffoldValue,
        directive = navigator.scaffoldDirective,
        listPane = {
            AnimatedPane(
                modifier = Modifier
                    .preferredWidth(260.dp)
            ) {
                WeatherLocationsListScreen(
                    uiState = homeUiState,
                    particleTick = particleTick.toLong(),
                    navigator = navigator,
                    showLocationPermissionRequest = showLocationPermissionRequest,
                    onItemClick = { location ->
                        coroutineScope.launch {
                            onLocationSelected(location)
                            onNavigateToLocation(location.id)
                            pagerState.scrollToPage(homeUiState.locationsList.indexOf(location))
                        }
                    },
                    onSwipeRefresh = onSwipeRefresh,
                    onDeleteLocation = onDeleteLocation,
                    onSearchLocationClick = onAddLocation,
                    onRequestPermission = onRequestPermission,
                    onOrderChanged = onOrderChanged,
                    onNavigateToLocation = onNavigateToLocation,
                    animatedVisibilityScope = animatedVisibilityScope,
                    sharedTransitionScope = sharedTransitionScope,
                )
            }
        },
        detailPane = {
            if (navigator.currentDestination?.contentKey != null) {
                AnimatedPane(
                    enterTransition = slideInHorizontally() + fadeIn(),
                    exitTransition = slideOutHorizontally() + fadeOut()
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .nestedScroll(exitAlwaysScrollBehavior),
                    ) {
                        if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
                            homeUiState.locationsList.getOrNull(pagerState.currentPage)?.let {
                                WeatherAnimationsBackground(
                                    weatherLocation = it,
                                    particleTick = particleTick.toLong(),
                                )
                            }
                        }
                        HorizontalPager(
                            state = pagerState,
                            beyondViewportPageCount = 3,
                            modifier = Modifier,
                        ) { page ->
                            val alpha = 1f - abs(pagerState.currentPageOffsetFraction - (page - pagerState.currentPage)) * 0.7f
                            homeUiState.locationsList.getOrNull(page)?.let {
                                WeatherDetailsScreen(
                                    weatherLocation = it,
                                    isUpdating = homeUiState.isRefreshingLocations,
                                    onCloseClick = {
                                        coroutineScope.launch {
                                            navigator.navigateBack()
                                            onLocationSelected(null)
                                        }
                                    },
                                    onFullScreenModeChange = { fullScreen ->
                                        showDragHandle = fullScreen.not()
                                        if (fullScreen) {
                                            paneExpansionState.setFirstPaneProportion(0f)
                                        } else {
                                            paneExpansionState.setFirstPaneProportion(0.35f)
                                        }
                                    },
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    sharedTransitionScope = sharedTransitionScope,
                                    onDeleteLocationClicked = onDeleteLocationClicked,
                                    modifier = Modifier.alpha(alpha),
                                )
                            }
                        }
                        LocationBottomToolbar(
                            isCurrentLocation = homeUiState.selectedWeatherLocation?.isCurrentLocation == true,
                            canNavigateBack = pagerState.canScrollBackward,
                            canNavigateForward = pagerState.canScrollForward,
                            onNavigateBack = {
                                coroutineScope.launch {
                                    navigator.navigateBack()
                                    onLocationSelected(null)
                                }
                            },
                            scrollBehavior = exitAlwaysScrollBehavior,
                            onDeleteLocationClicked = onDeleteLocationClicked,
                            onPreviousLocationClicked = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            navSuiteType = navSuiteType,
                            onNextLocationClicked = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()),
                        )
                    }
                }
            }
        },
        paneExpansionDragHandle = {
            if (navigator.scaffoldValue.primary != PaneAdaptedValue.Hidden && showDragHandle) {
                Surface(
                    onClick = { },
                    shape = WeatherYouTheme.shapes.large,
                    shadowElevation = 6.dp,
                    interactionSource = paneInteractionSource,
                    color = if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
                        WeatherYouTheme.colorScheme.surface.copy(alpha = 0.4f)
                    } else {
                        WeatherYouTheme.colorScheme.primary
                    },
                    modifier = Modifier
                        .scale(
                            if (isPaneDragging) {
                                1.5f
                            } else {
                                1f
                            }
                        )
                        .size(
                            height = 40.dp,
                            width = 10.dp,
                        )
                        .paneExpansionDraggable(
                            state = paneExpansionState,
                            minTouchTargetSize = 0.dp,
                            interactionSource = paneInteractionSource,
                        ),
                ) { }
            }
        },
        paneExpansionState = paneExpansionState,
        modifier = if (WeatherYouTheme.themeSettings.showWeatherAnimations && navSuiteType == NavigationSuiteType.NavigationRail) {
            Modifier.background(
                Brush.verticalGradient(
                    homeUiState.getSelectedOrFirstLocation()?.getGradientList() ?: listOf(
                        WeatherYouTheme.colorScheme.background,
                        WeatherYouTheme.colorScheme.background,
                    )
                )
            )
        } else {
            Modifier.background(WeatherYouTheme.colorScheme.background)
        },
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LocationBottomToolbar(
    isCurrentLocation: Boolean,
    canNavigateBack: Boolean,
    canNavigateForward: Boolean,
    onNavigateBack: () -> Unit,
    navSuiteType: NavigationSuiteType ,
    scrollBehavior: FloatingToolbarScrollBehavior,
    onDeleteLocationClicked: () -> Unit,
    onPreviousLocationClicked: () -> Unit,
    onNextLocationClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    HorizontalFloatingToolbar(
        expanded = true,
        leadingContent = {
            AnimatedVisibility(navSuiteType != NavigationSuiteType.NavigationRail) {
                FilledIconButton(
                    onClick = onNavigateBack,
                    shapes = IconButtonDefaults.shapes(),
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.List,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "Locations",
                    )
                }
            }
        },
        trailingContent = {
            AnimatedVisibility(!isCurrentLocation) {
                IconButton(
                    onClick = onDeleteLocationClicked,
                    shapes = IconButtonDefaults.shapes(),
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete location"
                    )
                }
            }
        },
        colors = FloatingToolbarDefaults.standardFloatingToolbarColors(
            toolbarContainerColor = WeatherYouTheme.colorScheme.background.copy(alpha = 0.3f),
        ),
        expandedShadowElevation = 15.dp,
        scrollBehavior = scrollBehavior,
        modifier = modifier.padding(bottom = 16.dp),
    ) {
        IconButton(
            onClick = onPreviousLocationClicked,
            enabled = canNavigateBack,
            shapes = IconButtonDefaults.shapes(),
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Previous location"
            )
        }
        IconButton(
            onClick = onNextLocationClicked,
            enabled = canNavigateForward,
            shapes = IconButtonDefaults.shapes(),
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next location"
            )
        }
    }
}

@Composable
fun WeatherLocationsEmptyState(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 200.dp)
    ) {
        WeatherIcon(
            weatherCondition = WeatherCondition.PartlyCloudy,
            isDaylight = false,
            modifier = Modifier
                .size(120.dp)
                .padding(10.dp)
        )
        Text(
            text = stringResource(R.string.empty_locations),
            style = WeatherYouTheme.typography.headlineSmall,
            color = WeatherYouTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun RequestLocationPermission(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 200.dp)
    ) {
        Image(
            imageVector = Icons.Filled.Place,
            contentDescription = stringResource(R.string.location_image),
            modifier = Modifier
                .size(120.dp)
                .padding(10.dp),
            colorFilter = ColorFilter.tint(WeatherYouTheme.colorScheme.primary)
        )
        Text(
            text = stringResource(R.string.enable_location),
            style = WeatherYouTheme.typography.headlineSmall,
            color = WeatherYouTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = stringResource(R.string.enable_location_description),
            style = WeatherYouTheme.typography.titleSmall,
            color = WeatherYouTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = onRequestPermission,
            modifier = Modifier
        ) {
            Text(
                text = stringResource(R.string.grant_location_permission),
                style = WeatherYouTheme.typography.titleSmall,
                color = WeatherYouTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
fun DeleteLocationDialog(
    onDeleteLocationClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.delete_location_title))
        },
        confirmButton = {
            TextButton(onClick = onDeleteLocationClicked) {
                Text(text = stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}


@Composable
fun SearchLocationBar(
    onSearchLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = CircleShape,
        color = WeatherYouTheme.colorScheme.secondaryContainer,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
        onClick = onSearchLocationClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.padding(start = 16.dp),
                tint = WeatherYouTheme.colorScheme.primary,
            )

            Text(
                text = stringResource(id = R.string.search_location),
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                style = WeatherYouTheme.typography.bodyMedium,
                color = WeatherYouTheme.colorScheme.onSurface,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    WeatherYouTheme {
        SharedTransitionLayout {
            HomeScreen(
                homeUiState = HomeUiState(
                    locationsList = PreviewWeatherList,
                    selectedWeatherLocation = PreviewWeatherList.first(),
                ),
                pagerState = rememberPagerState { 0 },
                navigator = rememberListDetailPaneScaffoldNavigator<Int>(
                    calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(currentWindowAdaptiveInfo())
                ),
                showLocationPermissionRequest = false,
                onLocationSelected = { },
                onSwipeRefresh = { },
                onDeleteLocation = { },
                onDeleteLocationClicked = { },
                onAddLocation = { },
                onRequestPermission = { },
                onOrderChanged = { },
                onNavigateToLocation = { },
                animatedVisibilityScope = this@SharedTransitionLayout as AnimatedVisibilityScope,
                sharedTransitionScope = this,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Preview(device = Devices.PIXEL_C)
@Preview(device = Devices.PIXEL_C, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenWithLocationPreview() {
    WeatherYouTheme {
        SharedTransitionLayout {
            HomeScreen(
                homeUiState = HomeUiState(
                    locationsList = PreviewWeatherList,
                    selectedWeatherLocation = PreviewWeatherList.first(),
                ),
                pagerState = rememberPagerState { 0 },
                navigator = rememberListDetailPaneScaffoldNavigator<Int>(
                    calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(currentWindowAdaptiveInfo())
                ),
                showLocationPermissionRequest = false,
                onLocationSelected = { },
                onSwipeRefresh = { },
                onDeleteLocation = { },
                onDeleteLocationClicked = { },
                onAddLocation = { },
                onRequestPermission = { },
                onOrderChanged = { },
                onNavigateToLocation = { },
                animatedVisibilityScope = this@SharedTransitionLayout as AnimatedVisibilityScope,
                sharedTransitionScope = this,
            )
        }
    }
}