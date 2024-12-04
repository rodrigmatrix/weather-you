package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.annotation.SuppressLint
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.rodrigmatrix.weatheryou.components.WeatherYouLargeAppBar
import com.rodrigmatrix.weatheryou.components.WeatherYouSmallAppBar
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.details.FutureDaysForecast
import com.rodrigmatrix.weatheryou.components.particle.WeatherAnimationsBackground
import com.rodrigmatrix.weatheryou.components.preview.PreviewFutureDaysForecast
import com.rodrigmatrix.weatheryou.components.preview.PreviewHourlyForecast
import com.rodrigmatrix.weatheryou.components.preview.PreviewLightDark
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherLocation
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import org.koin.androidx.compose.getViewModel
import java.net.URI

@Composable
fun WeatherDetailsScreen(
    weatherLocation: WeatherLocation?,
    onCloseClick: () -> Unit,
    isFullScreen: Boolean,
    onDeleteLocationClicked: () -> Unit,
    viewModel: WeatherDetailsViewModel = getViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()

    viewModel.setWeatherLocation(weatherLocation)

    WeatherDetailsScreen(
        viewState = viewState,
        isFullScreen = isFullScreen,
        onExpandedButtonClick = viewModel::onFutureWeatherButtonClick,
        onCloseClick = onCloseClick,
        onDeleteClick = onDeleteLocationClicked
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WeatherDetailsScreen(
    viewState: WeatherDetailsViewState,
    isFullScreen: Boolean,
    onExpandedButtonClick: (Boolean) -> Unit,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            if (isFullScreen) {
                ExpandedTopAppBar(
                    viewState.weatherLocation?.name.orEmpty(),
                    onCloseClick,
                    onDeleteClick,
                    viewState.weatherLocation?.isCurrentLocation?.not() == true
                )
            } else {
                SmallScreenTopAppBar(
                    viewState.weatherLocation?.name.orEmpty(),
                    onCloseClick,
                    onDeleteClick,
                    viewState.weatherLocation?.isCurrentLocation?.not() == true
                )
            }
        },
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
            onExpandedButtonClick = onExpandedButtonClick,
            paddingValues = paddingValues,
        )
    }
}

@Composable
private fun WeatherDetailsContent(
    viewState: WeatherDetailsViewState,
    onExpandedButtonClick: (Boolean) -> Unit,
    paddingValues: PaddingValues,
) {
    val scrollState = rememberLazyListState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        state = scrollState,
        contentPadding = paddingValues,
    ) {
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
                    currentTime = weatherLocation.currentTime,
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
            contentDescription = "Apple Weather",
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = buildAnnotatedString {
                append("For more information, ")
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline
                    ),
                ) {
                    append(
                        "visit Apple Weather",
                    )
                }
            },
            style = WeatherYouTheme.typography.bodyMedium,
            color = WeatherYouTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable(onClick = {
                val intent = CustomTabsIntent.Builder()
                    .build()
                intent.launchUrl(context, Uri.parse("https://developer.apple.com/weatherkit/data-source-attribution/"))
            })
        )
        Spacer(Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallScreenTopAppBar(
    title: String,
    onCloseClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    showDeleteButton: Boolean
) {
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
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
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
            isFullScreen = false,
            onExpandedButtonClick = { },
            onCloseClick = {},
            onDeleteClick = {},
            modifier = Modifier.background(WeatherYouTheme.colorScheme.background)
        )
    }
}
