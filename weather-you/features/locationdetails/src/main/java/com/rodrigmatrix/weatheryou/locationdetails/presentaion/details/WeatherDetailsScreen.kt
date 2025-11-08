package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.net.Uri
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.rodrigmatrix.weatheryou.components.WeatherYouLargeAppBar
import com.rodrigmatrix.weatheryou.components.WeatherYouSmallAppBar
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.components.details.FutureDaysForecast
import com.rodrigmatrix.weatheryou.components.extensions.toGradientList
import com.rodrigmatrix.weatheryou.components.particle.WeatherAnimationsBackground
import com.rodrigmatrix.weatheryou.components.preview.PreviewFutureDaysForecast
import com.rodrigmatrix.weatheryou.components.preview.PreviewHourlyForecast
import com.rodrigmatrix.weatheryou.components.preview.PreviewLightDark
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherLocation
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.core.extensions.getDateTimeFromTimezone
import com.rodrigmatrix.weatheryou.core.state.WeatherYouAppState
import com.rodrigmatrix.weatheryou.domain.model.PressureTrend
import com.rodrigmatrix.weatheryou.domain.model.WeatherCard
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions.ConditionType
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions.ConditionsBottomSheet
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions.ConditionsViewModel
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions.TemperatureType
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState
import kotlin.collections.toMutableList
import kotlin.math.roundToInt

@OptIn(ExperimentalSharedTransitionApi::class)
@ExperimentalMaterial3Api
@Composable
fun WeatherDetailsScreen(
    weatherLocation: WeatherLocation?,
    isUpdating: Boolean,
    onCloseClick: () -> Unit,
    onDeleteLocationClicked: () -> Unit,
    onFullScreenModeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeatherDetailsViewModel = koinViewModel(key = weatherLocation?.id.toString()) {
        parametersOf(weatherLocation)
    },
    conditionsViewModel: ConditionsViewModel = koinViewModel(key = "conditions_${weatherLocation?.id.toString()}"),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    scrollState: ScrollState = rememberScrollState(),
    scaffoldState: SheetState = WeatherYouAppState.conditionsScaffoldState ?: rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            scrollState.value == 0
        }
    ),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val viewState by viewModel.viewState.collectAsState()
    val conditionsViewState by conditionsViewModel.viewState.collectAsState()

    WeatherYouTheme(
        themeMode = if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
            ThemeMode.Dark
        } else {
            WeatherYouTheme.themeMode
        },
        colorMode = WeatherYouTheme.colorMode,
        themeSettings = WeatherYouTheme.themeSettings,
    ) {
        with(sharedTransitionScope) {
            WeatherDetailsScreen(
                viewState = viewState,
                isUpdating = isUpdating,
                onExpandedButtonClick = viewModel::onFutureWeatherButtonClick,
                onCloseClick = onCloseClick,
                onDeleteClick = onDeleteLocationClicked,
                onOpenConditionsClick = { day, type, temperatureType ->
                    coroutineScope.launch {
                        conditionsViewModel.setConditions(
                            weatherLocation = viewState.weatherLocation!!,
                            day = day,
                            type = type,
                            temperatureType = temperatureType,
                        )
                        scrollState.scrollTo(0)
                        scaffoldState.expand()
                    }
                },
                onFullScreenModeChange = {
                    viewModel.onFullScreenModeChange(it)
                    onFullScreenModeChange(it)
                },
                onWeatherCardListOrderChange = viewModel::onWeatherCardListOrderChange,
                modifier = modifier,
            )
        }
    }

    if (conditionsViewState.weatherLocation != null) {
        ConditionsBottomSheet(
            viewState = conditionsViewState,
            bottomSheetState = scaffoldState,
            scrollState = scrollState,
            onTypeChange = conditionsViewModel::onTypeChange,
            onClick = {
                conditionsViewModel.setConditions(
                    weatherLocation = viewState.weatherLocation!!,
                    day = it,
                )
            },
            onTemperatureTypeChange = conditionsViewModel::onTemperatureTypeChange,
            onDismissRequest = {
                coroutineScope.launch {
                    conditionsViewModel.hideConditions()
                    scaffoldState.hide()
                }
            }
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun WeatherDetailsScreen(
    viewState: WeatherDetailsViewState,
    isUpdating: Boolean,
    onExpandedButtonClick: (Boolean) -> Unit,
    onOpenConditionsClick: (
        WeatherDay,
        ConditionType,
        TemperatureType,
    ) -> Unit,
    onFullScreenModeChange: (Boolean) -> Unit,
    onWeatherCardListOrderChange: (List<WeatherCard>) -> Unit,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        containerColor = if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
            Color.Transparent
        } else {
            WeatherYouTheme.colorScheme.background
        },
        modifier = modifier,
    ) { paddingValues ->
        WeatherDetailsContent(
            viewState = viewState,
            isUpdating = isUpdating,
            onExpandedButtonClick = onExpandedButtonClick,
            onOpenConditionsClick = onOpenConditionsClick,
            onCloseClick = onCloseClick,
            onDeleteClick = onDeleteClick,
            paddingValues = paddingValues,
            onFullScreenModeChange = onFullScreenModeChange,
            onWeatherCardListOrderChange = onWeatherCardListOrderChange,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun WeatherDetailsContent(
    viewState: WeatherDetailsViewState,
    isUpdating: Boolean,
    onExpandedButtonClick: (Boolean) -> Unit,
    onFullScreenModeChange: (Boolean) -> Unit,
    onOpenConditionsClick: (
        WeatherDay,
        ConditionType,
        TemperatureType,
    ) -> Unit,
    onWeatherCardListOrderChange: (List<WeatherCard>) -> Unit,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit,
    paddingValues: PaddingValues,
) {
    val view = LocalView.current
    var weatherCardDataList by remember { mutableStateOf(WeatherCard.entries.toList()) }
    LaunchedEffect(viewState.weatherCardList) {
        weatherCardDataList = viewState.weatherCardList
    }
    val listState = rememberLazyGridState()
    val reorderableLazyListState = rememberReorderableLazyGridState(
        lazyGridState = listState,
        scrollThresholdPadding = WindowInsets.statusBars.asPaddingValues(),
    ) { from, to ->
        weatherCardDataList = weatherCardDataList.toMutableList().apply {
            val fromIndex = indexOfFirst { it.name == from.key }
            val toIndex = indexOfFirst { it.name == to.key }
            if (fromIndex != -1 && toIndex != -1) {
                add(toIndex, removeAt(fromIndex))
            }
        }
        onWeatherCardListOrderChange(weatherCardDataList)
        view.performHapticAction(HapticAction.VirtualKey)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        state = listState,
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        }
        if (isUpdating) {
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = stringResource(R.string.updating_location),
                    style = WeatherYouTheme.typography.bodyMedium,
                    color = WeatherYouTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .windowInsetsTopHeight(WindowInsets.statusBars)
                        .fillMaxWidth(),
                )
            }
        }
        item(span = { GridItemSpan(2) }) {
            CurrentWeather(
                weatherLocation = viewState.weatherLocation!!,
            )
        }
        items(
            items = weatherCardDataList,
            span = { item ->
                if (item.fullSpan) {
                    GridItemSpan(2)
                } else {
                    GridItemSpan(1)
                }
            },
            key = { item -> item.name },
        ) { item ->
            ReorderableItem(
                state = reorderableLazyListState,
                key = item.name,
            ) { isDragging ->
                val scale by animateFloatAsState(if (isDragging) 1.1f else 1f, label = "")
                WeatherCardItem(
                    item = item,
                    viewState = viewState,
                    onOpenConditionsClick = onOpenConditionsClick,
                    modifier = Modifier
                        .longPressDraggableHandle(
                            onDragStarted = {
                                view.performHapticAction(HapticAction.DragStart)
                            },
                            onDragStopped = {
                                view.performHapticAction(HapticAction.DragEnd)
                            },
                        )
                        .scale(scale),
                )
            }
        }
        item(span = { GridItemSpan(2) }) {
            AppleWeatherAttribution()
        }
    }
}

@Composable
fun WeatherCardItem(
    item: WeatherCard,
    viewState: WeatherDetailsViewState,
    onOpenConditionsClick: (
        WeatherDay,
        ConditionType,
        TemperatureType
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    val weatherLocation = viewState.weatherLocation!!
    when (item) {
        WeatherCard.Hours -> HourlyForecast(
            hoursList = viewState.todayWeatherHoursList,
            onClick = {
                onOpenConditionsClick(
                    viewState.weatherLocation.days.first(),
                    ConditionType.Conditions,
                    TemperatureType.Actual,
                )
            },
            modifier = modifier,
        )
        WeatherCard.FutureDays -> FutureDaysForecast(
            futureDaysList = viewState.futureDaysList,
            maxWeekTemperature = viewState.weatherLocation.maxWeekTemperature,
            minWeekTemperature = viewState.weatherLocation.minWeekTemperature,
            currentTemperature = viewState.weatherLocation.currentWeather,
            isExpanded = viewState.isFutureWeatherExpanded,
            onExpandedButtonClick = { },
            onExpandDay = {
                onOpenConditionsClick(it, ConditionType.Conditions, TemperatureType.Actual)
            },
            modifier = modifier,
        )
        WeatherCard.Wind -> WindCard(
            windSpeed = viewState.weatherLocation.windSpeed,
            windDirection = viewState.weatherLocation.windDirection,
            windGustSpeed = viewState.weatherLocation.windGust,
            onClick = {
                onOpenConditionsClick(
                    viewState.weatherLocation.days.first(),
                    ConditionType.Wind,
                    TemperatureType.Actual,
                )
            },
            modifier = modifier,
        )
        WeatherCard.FeelsLike -> FeelsLikeCard(
            actual = viewState.weatherLocation.currentWeather,
            feelsLike = viewState.weatherLocation.feelsLike,
            onClick = {
                onOpenConditionsClick(
                    viewState.weatherLocation.days.first(),
                    ConditionType.Conditions,
                    TemperatureType.FeelsLike,
                )
            },
            modifier = modifier,
        )
        WeatherCard.Visibility -> VisibilityCard(
            visibility = viewState.weatherLocation.visibility,
            onClick = {
//                onOpenConditionsClick(
//                    viewState.weatherLocation.days.first(),
//                    ConditionType.Visibility,
//                    TemperatureType.Actual,
//                )
            },
            modifier = modifier,
        )
        WeatherCard.UvIndex -> UvIndexCard(
            uvIndex = viewState.weatherLocation.uvIndex,
            onClick = {
                onOpenConditionsClick(
                    viewState.weatherLocation.days.first(),
                    ConditionType.UvIndex,
                    TemperatureType.Actual,
                )
            },
            modifier = modifier,
        )
        WeatherCard.Pressure -> PressureCard(
            pressure = viewState.weatherLocation.pressure,
            trend = viewState.weatherLocation.pressureTrend,
            onClick = {
//                onOpenConditionsClick(
//                    viewState.weatherLocation.days.first(),
//                    ConditionType.Pressure,
//                    TemperatureType.Actual,
//                )
            },
            modifier = modifier,
        )
        WeatherCard.Humidity -> HumidityCard(
            humidity = viewState.weatherLocation.humidity,
            dewPoint = viewState.weatherLocation.dewPoint,
            onClick = {
//                onOpenConditionsClick(
//                    viewState.weatherLocation.days.first(),
//                    ConditionType.Humidity,
//                    TemperatureType.Actual,
//                )
            },
            modifier = modifier,
        )
        WeatherCard.SunriseSunset -> SunriseSunsetCard(
            sunrise = weatherLocation.sunrise,
            sunset = weatherLocation.sunset,
            currentTime = weatherLocation.timeZone.getDateTimeFromTimezone(),
            isDaylight = weatherLocation.isDaylight,
            modifier = modifier,
        )
    }
}

@Composable
fun AppleWeatherAttribution(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(
                when {
                    WeatherYouTheme.themeSettings.showWeatherAnimations -> com.rodrigmatrix.weatheryou.locationdetails.R.drawable.ic_apple_weather_light
                    WeatherYouTheme.isDarkTheme -> com.rodrigmatrix.weatheryou.locationdetails.R.drawable.ic_apple_weather_light
                    else -> com.rodrigmatrix.weatheryou.locationdetails.R.drawable.ic_apple_weather_dark
                }
            ),
            contentDescription = stringResource(R.string.apple_weather),
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.for_more_information))
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline
                    ),
                ) {
                    append(stringResource(R.string.visit_apple_weather))
                }
            },
            style = WeatherYouTheme.typography.bodyMedium,
            color = WeatherYouTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable(
                    onClick = {
                        val intent = CustomTabsIntent
                            .Builder()
                            .build()
                        intent.launchUrl(
                            context,
                            Uri.parse("https://developer.apple.com/weatherkit/data-source-attribution/")
                        )
                    }
                )
        )
        Spacer(Modifier.height(32.dp))
    }
}

@ExperimentalMaterial3Api
@Composable
fun SmallScreenTopAppBar(
    title: String,
    onCloseClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onFullScreenModeChange: (Boolean) -> Unit,
    showDeleteButton: Boolean,
    isFullScreenMode: Boolean,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val navSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
    WeatherYouSmallAppBar(
        title = {
            Text(
                text = "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = WeatherYouTheme.typography.bodyLarge,
                color = WeatherYouTheme.colorScheme.onBackground,
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = WeatherYouTheme.colorScheme.primary,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            Row {
                if (navSuiteType != NavigationSuiteType.NavigationBar) {
                    Crossfade(
                        if (isFullScreenMode) {
                            com.rodrigmatrix.weatheryou.locationdetails.R.drawable.ic_close_fullscreen
                        } else {
                            com.rodrigmatrix.weatheryou.locationdetails.R.drawable.ic_open_in_full
                        }, label = "fullscreen_icon"
                    ) { icon ->
                        IconButton(onClick = {
                            onFullScreenModeChange(!isFullScreenMode)
                        }) {
                            Icon(
                                painter = painterResource(icon),
                                tint = WeatherYouTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp),
                                contentDescription = null,
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                }
                if (showDeleteButton) {
                    IconButton(onClick = onDeleteButtonClick) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            tint = WeatherYouTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp),
                            contentDescription = stringResource(R.string.delete_location)
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun ExpandedTopAppBar(
    title: String,
    onCloseClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    showDeleteButton: Boolean
) {
    WeatherYouLargeAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = WeatherYouTheme.typography.bodyLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    tint = WeatherYouTheme.colorScheme.primary,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            if (showDeleteButton) {
                IconButton(onClick = onDeleteButtonClick) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        tint = WeatherYouTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp),
                        contentDescription = stringResource(R.string.delete_location)
                    )
                }
            }
        },
        modifier = Modifier.animateContentSize()
    )
}

private fun View.performHapticAction(action: HapticAction) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        performHapticFeedback(
            when (action) {
                HapticAction.VirtualKey -> HapticFeedbackConstants.VIRTUAL_KEY
                HapticAction.DragStart -> HapticFeedbackConstants.DRAG_START
                HapticAction.DragEnd -> HapticFeedbackConstants.GESTURE_END
            }
        )
    } else {
        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
}

enum class HapticAction {
    VirtualKey,
    DragStart,
    DragEnd,
}

@ExperimentalMaterial3Api
@PreviewLightDark
@Composable
fun WeatherDetailsScreenPreview() {
    WeatherYouTheme {
        WeatherDetailsScreen(
            viewState = WeatherDetailsViewState(
                weatherLocation = PreviewWeatherLocation,
                todayWeatherHoursList = PreviewHourlyForecast,
                futureDaysList = PreviewFutureDaysForecast,
            ),
            isUpdating = true,
            onExpandedButtonClick = { },
            onCloseClick = {},
            onDeleteClick = {},
            onOpenConditionsClick = { _, _, _ -> },
            onFullScreenModeChange = { },
            onWeatherCardListOrderChange = { },
            modifier = Modifier.background(WeatherYouTheme.colorScheme.background)
        )
    }
}
