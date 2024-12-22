package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
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
import com.rodrigmatrix.weatheryou.components.R
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
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions.ConditionsBottomSheet
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions.ConditionsViewModel
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
import org.koin.androidx.compose.getViewModel
import kotlin.math.roundToInt

@ExperimentalMaterial3Api
@Composable
fun WeatherDetailsScreen(
    weatherLocation: WeatherLocation?,
    isUpdating: Boolean,
    onCloseClick: () -> Unit,
    onDeleteLocationClicked: () -> Unit,
    onFullScreenModeChange: (Boolean) -> Unit,
    viewModel: WeatherDetailsViewModel = getViewModel(),
    conditionsViewModel: ConditionsViewModel = getViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: SheetState = WeatherYouAppState.conditionsScaffoldState ?: rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    ),
    modifier: Modifier = Modifier,
) {
    val viewState by viewModel.viewState.collectAsState()
    val conditionsViewState by conditionsViewModel.viewState.collectAsState()

    viewModel.setWeatherLocation(weatherLocation)
    WeatherYouTheme(
        themeMode = if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
            ThemeMode.Dark
        } else {
            WeatherYouTheme.themeMode
        },
        colorMode = WeatherYouTheme.colorMode,
        themeSettings = WeatherYouTheme.themeSettings,
    ) {
        WeatherDetailsScreen(
            viewState = viewState,
            isUpdating = isUpdating,
            onExpandedButtonClick = viewModel::onFutureWeatherButtonClick,
            onCloseClick = onCloseClick,
            onDeleteClick = onDeleteLocationClicked,
            onExpandDay = {
                coroutineScope.launch {
                    conditionsViewModel.setConditions(
                        weatherLocation = viewState.weatherLocation!!,
                        day = it,
                    )
                    scaffoldState.expand()
                }
            },
            onFullScreenModeChange = {
                viewModel.onFullScreenModeChange(it)
                onFullScreenModeChange(it)
            },
            modifier = modifier,
        )
    }

    if (conditionsViewState.weatherLocation != null) {
        Column {
            Spacer(Modifier.height(20.dp))
            ConditionsBottomSheet(
                viewState = conditionsViewState,
                bottomSheetState = scaffoldState,
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
}

@ExperimentalMaterial3Api
@Composable
fun WeatherDetailsScreen(
    viewState: WeatherDetailsViewState,
    isUpdating: Boolean,
    onExpandedButtonClick: (Boolean) -> Unit,
    onExpandDay: (WeatherDay) -> Unit,
    onFullScreenModeChange: (Boolean) -> Unit,
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
        if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
            viewState.weatherLocation?.let {
                WeatherAnimationsBackground(
                    weatherLocation = viewState.weatherLocation,
                )
            }
        }
        WeatherDetailsContent(
            viewState = viewState,
            isUpdating = isUpdating,
            onExpandedButtonClick = onExpandedButtonClick,
            onExpandDay = onExpandDay,
            onCloseClick = onCloseClick,
            onDeleteClick = onDeleteClick,
            paddingValues = paddingValues,
            onFullScreenModeChange = onFullScreenModeChange,
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
    onExpandDay: (WeatherDay) -> Unit,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit,
    paddingValues: PaddingValues,
) {
    val scrollState = rememberLazyListState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        state = scrollState,
    ) {
        item {
            SmallScreenTopAppBar(
                title = viewState.weatherLocation?.name.orEmpty(),
                onCloseClick = onCloseClick,
                onDeleteButtonClick = onDeleteClick,
                onFullScreenModeChange = onFullScreenModeChange,
                isFullScreenMode = viewState.isFullScreenMode,
                showDeleteButton = viewState.weatherLocation?.isCurrentLocation?.not() == true,
            )
        }
        if (isUpdating) {
            item {
                Text(
                    text = stringResource(R.string.updating_location),
                    style = WeatherYouTheme.typography.bodyMedium,
                    color = WeatherYouTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }
        item {
            viewState.weatherLocation?.let {
                CurrentWeather(
                    it,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
        item {
            HourlyForecast(
                hoursList = viewState.todayWeatherHoursList,
                onClick = { onExpandDay(viewState.weatherLocation?.days?.first()!!) },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        item {
            FutureDaysForecast(
                futureDaysList = viewState.futureDaysList,
                maxWeekTemperature = viewState.weatherLocation?.maxWeekTemperature ?: 0.0,
                minWeekTemperature = viewState.weatherLocation?.minWeekTemperature ?: 0.0,
                currentTemperature = viewState.weatherLocation?.currentWeather ?: 0.0,
                isExpanded = viewState.isFutureWeatherExpanded,
                onExpandedButtonClick = onExpandedButtonClick,
                onExpandDay = onExpandDay,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        item {
            Row {
                Column(Modifier.weight(1f)) {
                    WindCard(
                        viewState.weatherLocation?.windSpeed ?: 0.0,
                        viewState.weatherLocation?.windDirection ?: 0.0,
                        modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    )
                }
                Column(Modifier.weight(1f)) {
                    HumidityCard(
                        viewState.weatherLocation?.humidity ?: 0.0,
                        viewState.weatherLocation?.dewPoint ?: 0.0,
                        modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                    )
                }
            }
        }
        item {
            Row {
                Column(Modifier.weight(1f)) {
                    VisibilityCard(
                        viewState.weatherLocation?.visibility ?: 0.0,
                        modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    )
                }
                Column(Modifier.weight(1f)) {
                    UvIndexCard(
                        viewState.weatherLocation?.uvIndex ?: 0.0,
                        modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                    )
                }
            }
        }
        item {
            viewState.weatherLocation?.let { weatherLocation ->
                SunriseSunsetCard(
                    sunrise = weatherLocation.sunrise,
                    sunset = weatherLocation.sunset,
                    currentTime = weatherLocation.timeZone.getDateTimeFromTimezone(),
                    isDaylight = weatherLocation.isDaylight,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            Spacer(Modifier.height(10.dp))
        }
        item {
            AppleWeatherAttribution()
        }
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
        Spacer(Modifier.height(16.dp))
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
            onExpandDay = { },
            onFullScreenModeChange = { },
            modifier = Modifier.background(WeatherYouTheme.colorScheme.background)
        )
    }
}
