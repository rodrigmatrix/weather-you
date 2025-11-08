package com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.details.color.bft_0_color
import com.rodrigmatrix.weatheryou.components.details.color.bft_10_color
import com.rodrigmatrix.weatheryou.components.details.color.bft_11_color
import com.rodrigmatrix.weatheryou.components.details.color.bft_12_color
import com.rodrigmatrix.weatheryou.components.details.color.bft_1_color
import com.rodrigmatrix.weatheryou.components.details.color.bft_2_color
import com.rodrigmatrix.weatheryou.components.details.color.bft_3_color
import com.rodrigmatrix.weatheryou.components.details.color.bft_4_color
import com.rodrigmatrix.weatheryou.components.details.color.bft_5_color
import com.rodrigmatrix.weatheryou.components.details.color.bft_6_color
import com.rodrigmatrix.weatheryou.components.details.color.bft_7_color
import com.rodrigmatrix.weatheryou.components.details.color.bft_8_color
import com.rodrigmatrix.weatheryou.components.details.color.bft_9_color
import com.rodrigmatrix.weatheryou.components.details.extensions.uvIndexAlertStringRes
import com.rodrigmatrix.weatheryou.components.details.extensions.uvIndexStringRes
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherLocation
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.components.theme.weatherTextColor
import com.rodrigmatrix.weatheryou.core.extensions.convertSpeed
import com.rodrigmatrix.weatheryou.core.extensions.getDateTimeFromTimezone
import com.rodrigmatrix.weatheryou.core.extensions.getHourString
import com.rodrigmatrix.weatheryou.core.extensions.getLocalTime
import com.rodrigmatrix.weatheryou.core.extensions.speedString
import com.rodrigmatrix.weatheryou.core.extensions.speedUnitString
import com.rodrigmatrix.weatheryou.core.extensions.windDirectionIndicator
import com.rodrigmatrix.weatheryou.core.state.WeatherYouAppState
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
import kotlin.math.roundToInt

@Composable
fun WindContent(
    viewState: ConditionsViewState,
    weatherLocation: WeatherLocation,
    day: WeatherDay,
    onPopupStateChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var uvIndexPopup: PopupValue? by remember { mutableStateOf(null) }
    Column {
        if (uvIndexPopup != null) {
            PopupUvIndex(
                popup = uvIndexPopup!!,
                hour = day.hours[uvIndexPopup!!.dataIndex],
            )
        } else {
            if (weatherLocation.days.indexOf(day) == 0) {
                ConditionHeader(
                    windSpeed = weatherLocation.windSpeed,
                    windGust = weatherLocation.windGust,
                    windDirection = weatherLocation.windDirection,
                    modifier = modifier.padding(horizontal = 16.dp),
                )
            } else {
                FutureConditionHeader(
                    weatherDay = viewState.day!!,
                    modifier = modifier.padding(horizontal = 16.dp),
                )
            }
        }
        Spacer(Modifier.height(40.dp))
        CurrentDayGraphBox(
            weatherLocation = weatherLocation,
            day = day,
        ) {
            WindChart(
                day = day,
                onPopupDisplay = {
                    uvIndexPopup = it
                    onPopupStateChange(it != null)
                },
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.bft_scale),
            color = WeatherYouTheme.colorScheme.weatherTextColor,
            style = WeatherYouTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(10.dp))

        BeaufortScaleTable()

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun WindChart(
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
                    values = day.hours.map { it.windSpeed },
                    color = Brush.horizontalGradient(
                        day.hours.map { it.windSpeed }.toWindGradientList()
                    ),
                    curvedEdges = true,
                    strokeAnimationSpec = tween(300, easing = EaseInOutCubic),
                    gradientAnimationDelay = 0,
                ),
                Line(
                    label = "",
                    drawStyle = DrawStyle.Stroke(8.dp),
                    values = day.hours.map { it.windGust },
                    color = Brush.horizontalGradient(
                        day.hours.map { it.windSpeed }.toWindGradientList()
                    ),
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
            },
        ),
        labelHelperProperties = LabelHelperProperties(enabled = false),
        minValue = 0.0,
        maxValue = 80.0,
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

private data class BeaufortData(
    val bft: Int,
    val description: String,
    val speedRange: String,
    val color: Color,
)

@Composable
private fun getBeaufortScaleData(): List<BeaufortData> {
    return listOf(
        BeaufortData(0, stringResource(R.string.bft_desc_0), "< 2", bft_0_color),
        BeaufortData(1, stringResource(R.string.bft_desc_1), "2 - 5", bft_1_color),
        BeaufortData(2, stringResource(R.string.bft_desc_2), "6 - 11", bft_2_color),
        BeaufortData(3, stringResource(R.string.bft_desc_3), "12 - 19", bft_3_color),
        BeaufortData(4, stringResource(R.string.bft_desc_4), "20 - 28", bft_4_color),
        BeaufortData(5, stringResource(R.string.bft_desc_5), "29 - 38", bft_5_color),
        BeaufortData(6, stringResource(R.string.bft_desc_6), "39 - 49", bft_6_color),
        BeaufortData(7, stringResource(R.string.bft_desc_7), "50 - 61", bft_7_color),
        BeaufortData(8, stringResource(R.string.bft_desc_8), "62 - 74", bft_8_color),
        BeaufortData(9, stringResource(R.string.bft_desc_9), "75 - 87", bft_9_color),
        BeaufortData(10, stringResource(R.string.bft_desc_10), "88 - 102", bft_10_color),
        BeaufortData(11, stringResource(R.string.bft_desc_11), "103 - 117", bft_11_color),
        BeaufortData(12, stringResource(R.string.bft_desc_12), "> 118", bft_12_color)
    )
}

@Composable
fun BeaufortScaleTable(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        BeaufortTableHeader()
        getBeaufortScaleData().forEach { data ->
            HorizontalDivider(color = WeatherYouTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            BeaufortTableRow(data = data)
        }
    }
}

@Composable
private fun BeaufortTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp,
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.bft_scale_short),
            style = WeatherYouTheme.typography.bodyMedium,
            color = WeatherYouTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.25f)
        )
        Text(
            text = stringResource(R.string.description),
            color = WeatherYouTheme.colorScheme.onBackground,
            style = WeatherYouTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.45f)
        )
        Text(
            text = speedUnitString(),
            style = WeatherYouTheme.typography.bodyMedium,
            color = WeatherYouTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.3f)
        )
    }
}

@Composable
private fun BeaufortTableRow(data: BeaufortData) {
    val windUnitPreference = WeatherYouAppState.appSettings.windUnitPreference
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(0.25f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(data.color, CircleShape)
            )
            Text(
                text = data.bft.toString(),
                color = WeatherYouTheme.colorScheme.onBackground,
                style = WeatherYouTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        Text(
            text = data.description,
            style = WeatherYouTheme.typography.bodyMedium,
            color = WeatherYouTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(0.45f)
                .padding(horizontal = 4.dp)
        )

        Text(
            text = data.speedRange.replace(Regex("[0-9]+")) {
                it.value.toDouble().convertSpeed(windUnitPreference)
            },
            style = WeatherYouTheme.typography.bodyMedium,
            color = WeatherYouTheme.colorScheme.onBackground,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.3f)
        )
    }
}


@Preview
@Composable
private fun BeaufortScaleTablePreview() {
    WeatherYouTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BeaufortScaleTable()
        }
    }
}

@Composable
private fun PopupUvIndex(
    popup: PopupValue,
    hour: WeatherHour,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    ChartPopupHeader(
        popupValue = popup,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = hour.dateTime.getHourString(context),
                style = WeatherYouTheme.typography.bodySmall,
                color = WeatherYouTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            )
            ConditionHeader(
                windSpeed = hour.windSpeed,
                windGust = hour.windGust,
                windDirection = hour.windDirection.toDouble(),
            )
        }
    }
}

@Composable
private fun ConditionHeader(
    windSpeed: Double,
    windGust: Double,
    windDirection: Double,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = windSpeed.speedString(),
                style = WeatherYouTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = WeatherYouTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = windDirection.windDirectionIndicator(),
                style = WeatherYouTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = WeatherYouTheme.colorScheme.onBackground,
            )
        }
        Text(
            text = "Gusts: " + windGust.speedString(),
            style = WeatherYouTheme.typography.bodyLarge,
            color = WeatherYouTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun FutureConditionHeader(
    weatherDay: WeatherDay,
    modifier: Modifier = Modifier,
) {
    val windUnitPreference = WeatherYouAppState.appSettings.windUnitPreference
    Column(modifier = modifier) {
        Text(
            text = buildAnnotatedString {
                append(weatherDay.hours.minOf { it.windSpeed }.convertSpeed(windUnitPreference))
                append("-")
                append(weatherDay.hours.maxOf { it.windSpeed }.speedString())
            },
            style = WeatherYouTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = WeatherYouTheme.colorScheme.onBackground,
        )
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.gusts))
                append(": ")
                append(weatherDay.hours.minOf { it.windGust }.convertSpeed(windUnitPreference))
                append("-")
                append(weatherDay.hours.maxOf { it.windGust }.speedString())
            },
            style = WeatherYouTheme.typography.bodyLarge,
            color = WeatherYouTheme.colorScheme.onBackground,
        )
    }
}

fun List<Double>.toWindGradientList(): List<Color> {
    return this.map { value ->
        when (value.roundToInt()) {
            in 0..1 -> bft_0_color
            in 2..5 -> bft_1_color
            in 6..11 -> bft_2_color
            in 12..19 -> bft_3_color
            in 20..28 -> bft_4_color
            in 29..38 -> bft_5_color
            in 39..49 -> bft_6_color
            in 50..61 -> bft_7_color
            in 62..74 -> bft_8_color
            in 75..87 -> bft_9_color
            in 88..102 -> bft_10_color
            in 103..117 -> bft_11_color
            else -> bft_12_color
        }
    }
}

@Preview
@Composable
fun WindContentPreview() {
    WindContent(
        viewState = ConditionsViewState(),
        weatherLocation = PreviewWeatherLocation,
        onPopupStateChange = { },
        day = PreviewWeatherLocation.days.first(),
    )
}
