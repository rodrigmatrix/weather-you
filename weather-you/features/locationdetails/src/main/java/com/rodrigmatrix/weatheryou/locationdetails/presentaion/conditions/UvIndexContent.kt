package com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.details.extensions.uvIndexAlertStringRes
import com.rodrigmatrix.weatheryou.components.details.extensions.uvIndexStringRes
import com.rodrigmatrix.weatheryou.components.details.getUvIndexGradient
import com.rodrigmatrix.weatheryou.components.details.toUvIndexGradientList
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherLocation
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.components.theme.weatherTextColor
import com.rodrigmatrix.weatheryou.core.extensions.getDateTimeFromTimezone
import com.rodrigmatrix.weatheryou.core.extensions.getHourString
import com.rodrigmatrix.weatheryou.core.extensions.getLocalTime
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import ir.ehsannarmani.compose_charts.LineChart
import com.rodrigmatrix.weatheryou.domain.model.MoonPhase
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationType
import ir.ehsannarmani.compose_charts.PopupValue
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import org.joda.time.DateTime

@Composable
fun UvIndexContent(
    viewState: ConditionsViewState,
    weatherLocation: WeatherLocation,
    day: WeatherDay,
    modifier: Modifier = Modifier,
) {
    var uvIndexPopup: PopupValue? by remember { mutableStateOf(null) }
    if (uvIndexPopup != null) {
        PopupUvIndex(
            popup = uvIndexPopup!!,
            hour = day.hours[uvIndexPopup!!.dataIndex],
        )
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
    CurrentDayGraphBox(
        weatherLocation = weatherLocation,
        day = day,
    ) {
        UvIndexChart(
            day = day,
            onPopupDisplay = { uvIndexPopup = it },
        )
    }

    Spacer(Modifier.height(20.dp))

    if (weatherLocation.days.indexOf(day) != 0) {
        val now = weatherLocation.timeZone.getDateTimeFromTimezone().getLocalTime()
        Text(
            text = stringResource(
                (day.hours.find { it.dateTime.hourOfDay == now.hourOfDay }?.uvIndex?.toInt() ?: 1).uvIndexAlertStringRes()
            ),
            color = WeatherYouTheme.colorScheme.weatherTextColor,
            style = WeatherYouTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }

    Spacer(Modifier.height(20.dp))
}

@Composable
private fun UvIndexChart(
    day: WeatherDay,
    onPopupDisplay: (PopupValue?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LineChart(
        modifier = modifier
            .height(200.dp)
            .padding(horizontal = 22.dp),
        data = remember(day) {
            listOf(
                Line(
                    label = "",
                    drawStyle = DrawStyle.Stroke(8.dp),
                    values = day.hours.map { it.uvIndex },
                    color = Brush.radialGradient(
                        day.hours.map { it.uvIndex }.toUvIndexGradientList()
                    ),
                    colors = getUvIndexGradient(
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
                it.toInt().toString()
            }
        ),
        labelHelperProperties = LabelHelperProperties(enabled = false),
        minValue = 0.0,
        maxValue = 11.0,
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
fun PopupUvIndex(
    popup: PopupValue,
    hour: WeatherHour,
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
            Text(
                text = popup.value.toInt().toString(),
                style = WeatherYouTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = WeatherYouTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(popup.value.toInt().uvIndexStringRes()),
                style = WeatherYouTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = WeatherYouTheme.colorScheme.onBackground,
            )
        }
    }
}


@Preview
@Composable
fun UvIndexContentPreview() {
    UvIndexContent(
        viewState = ConditionsViewState(),
        weatherLocation = PreviewWeatherLocation,
        day = PreviewWeatherLocation.days.first(),
    )
}


