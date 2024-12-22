package com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.extensions.getTemperatureGradient
import com.rodrigmatrix.weatheryou.components.extensions.toGradientList
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.components.theme.rain
import com.rodrigmatrix.weatheryou.components.theme.snow
import com.rodrigmatrix.weatheryou.core.extensions.getDateTimeFromTimezone
import com.rodrigmatrix.weatheryou.core.extensions.getFullDayString
import com.rodrigmatrix.weatheryou.core.extensions.getHourString
import com.rodrigmatrix.weatheryou.core.extensions.getLocalTime
import com.rodrigmatrix.weatheryou.core.extensions.percentageString
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.core.extensions.toTemperatureString
import com.rodrigmatrix.weatheryou.core.state.WeatherYouAppState
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationType
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.PopupValue
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import org.joda.time.Minutes
import kotlin.math.roundToInt

@Composable
fun ConditionsContent(
    weatherLocation: WeatherLocation,
    day: WeatherDay,
    viewState: ConditionsViewState,
    onTemperatureTypeChange: (TemperatureType) -> Unit,
    modifier: Modifier = Modifier,
) {
    var temperaturePopup: PopupValue? by remember { mutableStateOf(null) }
    var precipitationPopup: PopupValue? by remember { mutableStateOf(null) }
    if (temperaturePopup != null) {
        when (viewState.temperatureType) {
            TemperatureType.Actual -> {
                PopupCondition(
                    popup = temperaturePopup!!,
                    hour = day.hours[temperaturePopup!!.dataIndex],
                )
            }
            TemperatureType.FeelsLike -> {
                val hour = day.hours[temperaturePopup!!.dataIndex]
                PopupCondition(
                    popup = temperaturePopup!!,
                    hour = hour,
                    actualTemperature = hour.temperature,
                )
            }
        }
    } else {
        if (weatherLocation.days.indexOf(day) != 0) {
            ConditionHeader(
                day = day,
                temperatureType = viewState.temperatureType,
            )
        } else {
            TodayConditionHeader(
                weatherLocation = weatherLocation,
                day = day,
                temperatureType = viewState.temperatureType,
            )
        }
    }
    Spacer(Modifier.height(20.dp))
    Box {
        when (viewState.temperatureType) {
            TemperatureType.Actual -> ActualTemperatureChart(
                weatherLocation = weatherLocation,
                day = day,
                onPopupDisplay = { temperaturePopup = it },
            )
            TemperatureType.FeelsLike -> FeelsLikeChart(
                weatherLocation = weatherLocation,
                day = day,
                onPopupDisplay = { temperaturePopup = it },
            )
        }
        if (temperaturePopup != null) {
//                VerticalDivider(
//                    color = WeatherYouTheme.colorScheme.onBackground.copy(alpha = 0.8f),
//                    modifier = Modifier
//                        .height(200.dp)
//                        .offset(x = (popup?.position?.x!!).dp)
//                )
        }
        if (weatherLocation.days.indexOf(day) == 0) {
            val now = weatherLocation.timeZone.getDateTimeFromTimezone().getLocalTime()
            val start = weatherLocation.timeZone.getDateTimeFromTimezone().getLocalTime()
                .withHourOfDay(0)
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
            val end = weatherLocation.timeZone.getDateTimeFromTimezone().getLocalTime()
                .withHourOfDay(23)
                .withMinuteOfHour(59)
                .withSecondOfMinute(0)
            val totalMinutes = Minutes.minutesBetween(start, end).minutes
            val minutesPassed = Minutes.minutesBetween(start, now).minutes
            val percentage = try {
                ((minutesPassed * 100f) / totalMinutes) / 100f
            } catch (_: Exception) {
                0f
            }
            WeatherYouTheme(
                themeMode = ThemeMode.Dark,
                colorMode = WeatherYouTheme.colorMode,
                themeSettings = WeatherYouTheme.themeSettings,
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 55.dp, end = 20.dp)
                        .fillMaxWidth(percentage)
                        .height(200.dp)
                        .background(WeatherYouTheme.colorScheme.surface.copy(alpha = 0.7f))
                        .clip(RectangleShape),
                ) { }
            }
        }
    }

    Spacer(Modifier.height(20.dp))
    TemperatureTypeSelector(
        temperatureType = viewState.temperatureType,
        onClick = onTemperatureTypeChange,
        modifier = Modifier.padding(horizontal = 16.dp),
    )

    Spacer(Modifier.height(20.dp))

    HorizontalDivider(
        color = WeatherYouTheme.colorScheme.onBackground.copy(alpha = 0.12f),
    )

    Spacer(Modifier.height(20.dp))

    if (precipitationPopup == null) {
        Text(
            text = stringResource(R.string.chance_of_precipitation_title),
            style = WeatherYouTheme.typography.titleLarge,
            color = WeatherYouTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Text(
            text = stringResource(
                R.string.day_chance,
                day.dateTime.getFullDayString(),
                day.precipitationProbability.percentageString(),
            ),
            style = WeatherYouTheme.typography.bodyMedium,
            color = WeatherYouTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    } else {
        PopupPrecipitation(
            popup = precipitationPopup!!,
            hour = day.hours[precipitationPopup!!.dataIndex],
        )
    }

    Spacer(Modifier.height(20.dp))

    PrecipitationChart(
        day = day,
        onPopupDisplay = {
            precipitationPopup = it
        }
    )
    Spacer(Modifier.height(20.dp))

    Text(
        text = stringResource(R.string.precipitation_total),
        style = WeatherYouTheme.typography.titleLarge,
        color = WeatherYouTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(Modifier.height(20.dp))
    PrecipitationAmount(
        day = day,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}


@Composable
fun  PrecipitationAmount(
    day: WeatherDay,
    modifier: Modifier = Modifier,
) {
    val precipitationAmount = day.precipitationAmount
    val snowAmount = day.snowfallAmount
    Column(modifier = modifier) {
        if (precipitationAmount > 0.0) {
            PrecipitationItem(
                amount = precipitationAmount,
                type = PrecipitationType.Rain,
            )
        } else {
            PrecipitationItem(
                amount = 0.0,
                type = PrecipitationType.Clear,
            )
        }
        if (snowAmount > 0.0) {
            Spacer(Modifier.height(4.dp))
            PrecipitationItem(
                amount = snowAmount,
                type = PrecipitationType.Snow,
            )
        }
    }
}

@Composable
fun Double.toPrecipitationAmount(
    precipitationType: PrecipitationType
): String {
    val unit = when (precipitationType) {
        PrecipitationType.Snow -> R.string.centimeters
        else -> R.string.millimeters
    }
    val value = when (precipitationType) {
        PrecipitationType.Snow -> (this / 10).roundToInt()
        else -> this.roundToInt()
    }.toString()
    return value + " " + stringResource(unit)
}

@Composable
fun PrecipitationType.asString(): String {
    return stringResource(
        when (this) {
            PrecipitationType.Clear -> R.string.precipitation
            PrecipitationType.Precipitation -> R.string.precipitation
            PrecipitationType.Rain -> R.string.condition_rain
            PrecipitationType.Snow -> R.string.condition_snow
            PrecipitationType.Sleet -> R.string.precipitation
            PrecipitationType.Hail -> R.string.condition_hail
            PrecipitationType.Mixed -> R.string.precipitation
        }
    )
}

@Composable
fun PrecipitationItem(
    amount: Double,
    type: PrecipitationType,
    modifier: Modifier = Modifier,
) {
    val color = when (type) {
        PrecipitationType.Clear -> Color.Transparent
        PrecipitationType.Precipitation -> WeatherYouTheme.colorScheme.rain
        PrecipitationType.Rain -> WeatherYouTheme.colorScheme.rain
        PrecipitationType.Snow -> WeatherYouTheme.colorScheme.snow
        PrecipitationType.Sleet -> WeatherYouTheme.colorScheme.rain
        PrecipitationType.Hail -> WeatherYouTheme.colorScheme.rain
        PrecipitationType.Mixed -> WeatherYouTheme.colorScheme.rain
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        if (amount > 0.0) {
            Surface(
                color = color,
                shape = CircleShape,
                border = if (!WeatherYouTheme.isDarkTheme && type == PrecipitationType.Snow) {
                    BorderStroke(1.dp, Color.Black)
                } else {
                    null
                },
                content = { },
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(16.dp),
            )
        }

        Text(
            text = type.asString(),
            style = WeatherYouTheme.typography.titleMedium,
            color = WeatherYouTheme.colorScheme.onBackground,
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = amount.toPrecipitationAmount(type),
            style = WeatherYouTheme.typography.titleMedium,
            color = WeatherYouTheme.colorScheme.onBackground,
        )
    }
}

@Composable
fun ActualTemperatureChart(
    weatherLocation: WeatherLocation,
    day: WeatherDay,
    onPopupDisplay: (PopupValue?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val temperaturePreference = WeatherYouAppState.appSettings.temperaturePreference
    LineChart(
        modifier = modifier
            .height(200.dp)
            .padding(horizontal = 22.dp),
        data = remember(day) {
            listOf(
                Line(
                    label = "",
                    drawStyle = DrawStyle.Stroke(8.dp),
                    values = day.hours.map { it.temperature },
                    color = Brush.radialGradient(
                        day.hours.map { it.temperature }.toGradientList()
                    ),
                    colors = getTemperatureGradient(
                        minDayTemperature = day.minTemperature,
                        maxDayTemperature = day.maxTemperature,
                        hours = day.hours,
                    ).map {
                        it.copy(alpha = 0.5f)
                    }.asReversed(),
                    curvedEdges = true,
                    strokeAnimationSpec = tween(300, easing = EaseInOutCubic),
                    gradientAnimationDelay = 0,
                )
            )
        },
        indicatorProperties = HorizontalIndicatorProperties(
            enabled = true,
            textStyle = WeatherYouTheme.typography.bodyMedium.copy(
                color = WeatherYouTheme.colorScheme.onBackground
            ),
            contentBuilder = {
                it.toTemperatureString(temperaturePreference)
            }
        ),
        labelHelperProperties = LabelHelperProperties(enabled = false),
        minValue = (weatherLocation.days.minOf {
            it.minTemperature
        }) - 7.0,
        maxValue = (weatherLocation.days.maxOf {
            it.maxTemperature
        }) + 7.0,
        popupProperties = PopupProperties(
            containerColor = WeatherYouTheme.colorScheme.background,
            circleColor = Color.White,
            showCircle = true,
        ),
        gridProperties = GridProperties(
            enabled = true,
            yAxisProperties = GridProperties.AxisProperties(
                style = StrokeStyle.Dashed(),
            ),
        ),
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
        onPopupDisplay = onPopupDisplay,
    )
}


@Composable
fun FeelsLikeChart(
    weatherLocation: WeatherLocation,
    day: WeatherDay,
    onPopupDisplay: (PopupValue?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val temperaturePreference = WeatherYouAppState.appSettings.temperaturePreference
    val backgroundColor = WeatherYouTheme.colorScheme.background
    LineChart(
        modifier = modifier
            .height(200.dp)
            .padding(horizontal = 22.dp),
        data = remember(day) {
            listOf(
                Line(
                    label = "",
                    drawStyle = DrawStyle.Stroke(8.dp),
                    values = day.hours.map { it.temperature },
                    color = SolidColor(Color.Gray.copy(alpha = 0.4f)),
                    colors = listOf(
                        Color.Gray.copy(alpha = 0.4f),
                        Color.Gray.copy(alpha = 0.4f),
                    ),
                    curvedEdges = true,
                    strokeAnimationSpec = tween(300, easing = EaseInOutCubic),
                    gradientAnimationDelay = 0,
                    popupProperties = PopupProperties(enabled = false),
                ),
                Line(
                    label = "",
                    drawStyle = DrawStyle.Stroke(8.dp),
                    values = day.hours.map { it.feelsLike },
                    color = Brush.radialGradient(
                        day.hours.map { it.feelsLike }.toGradientList()
                    ),
                    curvedEdges = true,
                    strokeAnimationSpec = tween(300, easing = EaseInOutCubic),
                    gradientAnimationDelay = 0,
                    popupProperties = PopupProperties(
                        containerColor = backgroundColor,
                        circleColor = Color.White,
                        showCircle = true,
                    ),
                ),
            )
        },
        indicatorProperties = HorizontalIndicatorProperties(
            enabled = true,
            textStyle = WeatherYouTheme.typography.bodyMedium.copy(
                color = WeatherYouTheme.colorScheme.onBackground
            ),
            contentBuilder = {
                it.toTemperatureString(temperaturePreference)
            }
        ),
        gridProperties = GridProperties(
            enabled = true,
            yAxisProperties = GridProperties.AxisProperties(
                style = StrokeStyle.Dashed(),
            ),
        ),
        labelHelperProperties = LabelHelperProperties(enabled = false),
        minValue = (weatherLocation.days.minOf {
            it.minTemperature
        }) - 7.0,
        maxValue = (weatherLocation.days.maxOf {
            it.maxTemperature
        }) + 7.0,
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
        onPopupDisplay = onPopupDisplay,
    )
}

@Composable
fun PopupCondition(
    popup: PopupValue,
    hour: WeatherHour,
    actualTemperature: Double? = null,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val xOffset = (popup.position.x / 2.4)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.offset(x = if (xOffset <= screenWidth) {
            xOffset.dp
        } else {
            (screenWidth - 100).dp
        })
    ) {
        Text(
            text = hour.dateTime.getHourString(context),
            style = WeatherYouTheme.typography.bodySmall,
            color = WeatherYouTheme.colorScheme.onBackground.copy(alpha = 0.8f),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            WeatherIcon(
                weatherCondition = hour.weatherCondition,
                isDaylight = hour.isDaylight,
                alwaysStatic = true,
                modifier = Modifier.size(34.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = popup.value.temperatureString(),
                style = WeatherYouTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = WeatherYouTheme.colorScheme.onBackground,
            )
        }
        if (actualTemperature != null) {
            Text(
                text = "Actual: " + actualTemperature.temperatureString(),
                style = WeatherYouTheme.typography.bodySmall,
                color = WeatherYouTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            )
        }
    }
}


@Composable
fun PopupPrecipitation(
    popup: PopupValue,
    hour: WeatherHour,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.offset(x = (popup.position.x / 2.4).dp),
    ) {
        Text(
            text = hour.dateTime.getHourString(context),
            style = WeatherYouTheme.typography.bodySmall,
            color = WeatherYouTheme.colorScheme.onBackground.copy(alpha = 0.8f),
        )
        Text(
            text = popup.value.percentageString(),
            style = WeatherYouTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = WeatherYouTheme.colorScheme.onBackground,
        )
    }
}

@Composable
fun PrecipitationChart(
    day: WeatherDay,
    onPopupDisplay: (PopupValue?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val primaryColor = WeatherYouTheme.colorScheme.primary
    val backgroundColor = WeatherYouTheme.colorScheme.background
    val onBackgroundColor = WeatherYouTheme.colorScheme.onBackground
    val context = LocalContext.current
    LineChart(
        modifier = modifier
            .height(200.dp)
            .padding(horizontal = 22.dp),
        data = remember(day) {
            listOf(
                Line(
                    label = "",
                    drawStyle = DrawStyle.Stroke(2.dp),
                    values = day.hours.map { it.precipitationProbability },
                    color = SolidColor(primaryColor),
                    curvedEdges = false,
                    strokeAnimationSpec = tween(300, easing = EaseInOutCubic),
                    gradientAnimationDelay = 0,
                    popupProperties = PopupProperties(
                        enabled = true,
                        showCircle = true,
                        containerColor = backgroundColor,
                        circleColor = onBackgroundColor,
                    ),
                ),
            )
        },
        indicatorProperties = HorizontalIndicatorProperties(
            enabled = true,
            textStyle = WeatherYouTheme.typography.bodyMedium.copy(
                color = WeatherYouTheme.colorScheme.onBackground
            ),
            contentBuilder = {
                it.percentageString()
            }
        ),
        gridProperties = GridProperties(
            enabled = true,
            yAxisProperties = GridProperties.AxisProperties(
                style = StrokeStyle.Dashed(),
            ),
        ),
//        labelProperties = LabelProperties(
//            enabled = true,
//            labels = listOf("12 AM", "6 AM", "12 PM", "6 PM"),
//            textStyle = WeatherYouTheme.typography.bodySmall.copy(
//                color = WeatherYouTheme.colorScheme.onBackground
//            ),
//        ),
        labelHelperProperties = LabelHelperProperties(enabled = false),
        minValue = 0.0,
        maxValue = 100.0,
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
        onPopupDisplay = onPopupDisplay,
    )
}

@Composable
fun ConditionHeader(
    day: WeatherDay,
    temperatureType: TemperatureType,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text =  when (temperatureType) {
                        TemperatureType.Actual -> day.maxTemperature
                        TemperatureType.FeelsLike -> day.hours.maxOf { it.feelsLike }
                    }.temperatureString(),
                    style = WeatherYouTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = WeatherYouTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = when (temperatureType) {
                        TemperatureType.Actual -> day.minTemperature
                        TemperatureType.FeelsLike -> day.hours.minOf { it.feelsLike }
                    }.temperatureString(),
                    style = WeatherYouTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = WeatherYouTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                )
            }
            Text(
                text = when (WeatherYouAppState.appSettings.temperaturePreference) {
                    TemperaturePreference.METRIC -> stringResource(R.string.celsius)
                    TemperaturePreference.IMPERIAL -> stringResource(R.string.fahrenheit)
                },

                style = WeatherYouTheme.typography.bodyMedium,
                color = WeatherYouTheme.colorScheme.onBackground,
            )
        }
        Spacer(Modifier.width(4.dp))
        WeatherIcon(
            weatherCondition = day.weatherCondition,
            isDaylight = true,
            modifier = Modifier.size(34.dp)
        )
    }
}


@Composable
fun TodayConditionHeader(
    weatherLocation: WeatherLocation,
    day: WeatherDay,
    temperatureType: TemperatureType,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text =  when (temperatureType) {
                        TemperatureType.Actual -> weatherLocation.currentWeather
                        TemperatureType.FeelsLike -> weatherLocation.feelsLike
                    }.temperatureString(),
                    style = WeatherYouTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = WeatherYouTheme.colorScheme.onBackground,
                )
                WeatherIcon(
                    weatherCondition = day.weatherCondition,
                    isDaylight = true,
                    modifier = Modifier.size(34.dp)
                )
            }
            when (temperatureType) {
                TemperatureType.Actual -> {
                    Row {
                        Text(
                            text = stringResource(R.string.high_lowercase_x, day.maxTemperature.temperatureString()),
                            style = WeatherYouTheme.typography.bodyMedium,
                            color = WeatherYouTheme.colorScheme.onBackground,
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.low_lowercase_x, day.minTemperature.temperatureString()),
                            style = WeatherYouTheme.typography.bodyMedium,
                            color = WeatherYouTheme.colorScheme.onBackground,
                        )
                    }
                }
                TemperatureType.FeelsLike -> {
                    Text(
                        text = stringResource(R.string.actual_temperature, weatherLocation.currentWeather.temperatureString()),
                        style = WeatherYouTheme.typography.bodyMedium,
                        color = WeatherYouTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    }
}


@Composable
fun TemperatureTypeSelector(
    temperatureType: TemperatureType,
    onClick: (TemperatureType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedTabIndex = when (temperatureType) {
        TemperatureType.Actual -> 0
        TemperatureType.FeelsLike -> 1
    }
    val tabs = listOf(
        R.string.actual to TemperatureType.Actual,
        R.string.feels_like to TemperatureType.FeelsLike,
    )
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = WeatherYouTheme.colorScheme.surface,
        indicator = {

        },
        modifier = modifier.clip(WeatherYouTheme.shapes.small),
    ) {
        tabs.forEachIndexed { index, tab ->
            val selected = temperatureType == tabs[index].second
            Tab(
                selected = selected,
                onClick = { onClick(tab.second) },
                text = {
                    Text(
                        text = stringResource(tab.first),
                        maxLines = 1,
                        softWrap = false,
                        style = WeatherYouTheme.typography.bodyMedium,
                        color = if(selected)
                            WeatherYouTheme.colorScheme.onPrimaryContainer
                        else
                            WeatherYouTheme.colorScheme.onSurface,
                    )
                },
                modifier = Modifier
                    .height(30.dp)
                    .background(
                        if (selected)
                            WeatherYouTheme.colorScheme.primaryContainer
                        else
                            WeatherYouTheme.colorScheme.surface
                    ),
                selectedContentColor = WeatherYouTheme.colorScheme.primary,
                unselectedContentColor = WeatherYouTheme.colorScheme.surface,
            )
        }
    }
}