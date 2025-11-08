package com.rodrigmatrix.weatheryou.widgets.weather.animated

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.rodrigmatrix.weatheryou.components.extensions.getStaticIcon
import com.rodrigmatrix.weatheryou.components.extensions.getString
import com.rodrigmatrix.weatheryou.components.extensions.getTemperatureGradient
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherLocation
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.widgets.R
import com.rodrigmatrix.weatheryou.widgets.weather.utils.gradientBackground
import java.util.Calendar
import androidx.glance.ColorFilter
import androidx.glance.Image
import androidx.glance.GlanceTheme
import androidx.core.graphics.createBitmap
import androidx.glance.text.TextAlign
import org.joda.time.DateTime

@Composable
fun AnimatedMediumLargeWidget(
    weather: WeatherLocation,
    onWidgetClicked: Action,
    showDays: Boolean,
    width: Float,
    height: Float,
    daysCount: Int,
    glanceModifier: GlanceModifier = GlanceModifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = glanceModifier
            .background(ImageProvider(weather.gradientBackground(width, height)))
            .fillMaxSize()
            .cornerRadius(16.dp)
            .clickable(onWidgetClicked)
    ) {
        MediumWidgetHeader(weather)
        TemperatureTrend(
            maxToday = weather.maxTemperature,
            maxTomorrow = weather.days.drop(1).firstOrNull()?.maxTemperature ?: weather.maxTemperature,
            currentTime = weather.currentTime,
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        HoursList(
            hoursList = weather.hours,
        )
        if (showDays) {
            if (daysCount > 4) {
                Spacer(modifier = GlanceModifier.height(16.dp))
            }
            DaysList(
                daysList = weather.days,
                daysCount = daysCount,
                currentTemperature = weather.currentWeather,
                maxWeekTemperature = weather.maxWeekTemperature,
                minWeekTemperature = weather.minWeekTemperature,
            )
        } else {
            Spacer(modifier = GlanceModifier.height(8.dp))
        }
    }
}

@Composable
private fun MediumWidgetHeader(
    weather: WeatherLocation
) {
    Row(
        verticalAlignment = Alignment.Vertical.Top,
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = weather.name.substringBefore(","),
                    maxLines = 1,
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
                if (weather.isCurrentLocation) {
                    Spacer(GlanceModifier.width(4.dp))
                    Image(
                        provider = ImageProvider(com.rodrigmatrix.weatheryou.components.R.drawable.ic_my_location),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(ColorProvider(Color.White)),
                        modifier = GlanceModifier.size(20.dp)
                    )
                }
            }
            Text(
                text = weather.currentWeather.temperatureString(),
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
        }
        Spacer(modifier = GlanceModifier.defaultWeight())
        Column(horizontalAlignment = Alignment.End) {
            Image(
                provider = ImageProvider(weather.currentCondition.getStaticIcon(weather.isDaylight)),
                contentDescription = null,
                modifier = GlanceModifier.size(24.dp)
            )
            Text(
                text = LocalContext.current.getString(weather.currentCondition.getString(weather.isDaylight)),
                maxLines = 1,
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 16.sp,
                )
            )
            Row {
                Text(
                    text = weather.maxTemperature.temperatureString(),
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = GlanceModifier.width(4.dp))
                Text(
                    text = weather.lowestTemperature.temperatureString(),
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 16.sp,
                    )
                )
            }
        }
    }
}

@Composable
private fun HoursList(
    hoursList: List<WeatherHour>,
    modifier: GlanceModifier = GlanceModifier,
) {
    val context = LocalContext.current
    val currentTime = Calendar.getInstance()
    val pattern = if (DateFormat.is24HourFormat(context)) {
        "HH"
    } else {
        "hh aa"
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        hoursList.take(6).forEach { weatherHour ->
            currentTime.add(Calendar.HOUR, 1)
            HourRow(
                hour = weatherHour,
                hourString = DateFormat.format(pattern, currentTime.time)
                    .toString(),
                modifier = GlanceModifier.defaultWeight()
            )
        }
    }
}

@Composable
private fun HourRow(
    hour: WeatherHour,
    hourString: String,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = hourString,
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Image(
            provider = ImageProvider(hour.weatherCondition.getStaticIcon(hour.isDaylight)),
            contentDescription = null,
            modifier = GlanceModifier.size(36.dp)
        )
        Text(
            text = hour.temperature.temperatureString(),
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun DaysList(
    daysList: List<WeatherDay>,
    maxWeekTemperature: Double,
    minWeekTemperature: Double,
    currentTemperature: Double?,
    daysCount: Int,
    modifier: GlanceModifier = GlanceModifier,
) {
    Box(modifier = modifier
        .padding(
            horizontal = 8.dp,
            vertical = 8.dp,
        )
    ) {
        LazyColumn(
            modifier = modifier,
        ) {
            itemsIndexed(daysList.drop(1).take(daysCount)) { index, weatherDay ->
                Column {
                    DayRow(
                        day = weatherDay,
                        index = index,
                        maxWeekTemperature = maxWeekTemperature,
                        minWeekTemperature = minWeekTemperature,
                        currentTemperature = if (index == 0) {
                            currentTemperature
                        } else {
                            null
                        },
                    )
                    Spacer(modifier = GlanceModifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
private fun DayRow(
    currentTemperature: Double?,
    maxWeekTemperature: Double,
    minWeekTemperature: Double,
    day: WeatherDay,
    index: Int,
    modifier: GlanceModifier = GlanceModifier,
) {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_WEEK, index + 1)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = DateFormat.format("EEE", calendar.time).toString(),
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 18.sp,
            ),
            modifier = GlanceModifier.width(38.dp)
        )

        Spacer(modifier = GlanceModifier.width(8.dp))

        TemperatureGlanceBar(
            minWeekTemperature = minWeekTemperature,
            maxWeekTemperature = maxWeekTemperature,
            minDayTemperature = day.minTemperature,
            maxDayTemperature = day.maxTemperature,
            hours = day.hours,
            modifier = GlanceModifier.defaultWeight()
        )

        Image(
            provider = ImageProvider(day.weatherCondition.getStaticIcon(isDaylight = true)),
            contentDescription = null,
            modifier = GlanceModifier.size(24.dp)
        )
        Spacer(modifier = GlanceModifier.width(4.dp))
        Text(
            text = day.maxTemperature.temperatureString(),
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            ),
            modifier = GlanceModifier.width(30.dp)
        )
        Spacer(modifier = GlanceModifier.width(4.dp))
        Text(
            text = day.minTemperature.temperatureString(),
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            ),
            modifier = GlanceModifier.width(30.dp)
        )
    }
}

@Composable
fun TemperatureTrend(
    maxToday: Double,
    maxTomorrow: Double,
    currentTime: DateTime,
    modifier: GlanceModifier = GlanceModifier,
) {
    val context = LocalContext.current
    val trend = if (maxTomorrow - maxToday >= 5.0) {
        Pair(
            context.getString(
                com.rodrigmatrix.weatheryou.domain.R.string.weather_trend_up,
                maxTomorrow.temperatureString(),
            ),
            com.rodrigmatrix.weatheryou.components.R.drawable.ic_thermostat_arrow_up,
        )
    } else if (maxToday - maxTomorrow >= 5.0) {
        Pair(
            context.getString(
                com.rodrigmatrix.weatheryou.domain.R.string.weather_trend_down,
                maxTomorrow.temperatureString(),
            ),
            com.rodrigmatrix.weatheryou.components.R.drawable.ic_thermostat_arrow_down,
        )
    } else null
    if (trend != null && currentTime.hourOfDay >= 19) {
        Column(modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            Image(
                provider = ImageProvider(trend.second),
                contentDescription = null,
                modifier = GlanceModifier.size(26.dp),
            )
            Text(
                text = trend.first,
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(GlanceModifier.height(4.dp))
        }
    }
}

@Composable
fun MediumLargeLoading(
    onWidgetClicked: () -> Unit,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(ImageProvider(R.drawable.weather_outside_shape_widget))
            .fillMaxSize()
            .clickable(block = onWidgetClicked)
    ) {
        Text(
            text = "Loading Data...",
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun TemperatureGlanceBar(
    minWeekTemperature: Double,
    maxWeekTemperature: Double,
    minDayTemperature: Double,
    maxDayTemperature: Double,
    hours: List<WeatherHour>,
    modifier: GlanceModifier = GlanceModifier,
) {
    val context = LocalContext.current
    val barHeight = 8.dp

    val temperatureBarBitmap = remember(
        minWeekTemperature,
        maxWeekTemperature,
        minDayTemperature,
        maxDayTemperature,
        hours
    ) {
        val gradientColors = getTemperatureGradient(
            minDayTemperature = minDayTemperature,
            maxDayTemperature = maxDayTemperature,
            hours = hours,
        )
        createTemperatureBarBitmap(
            context = context,
            height = barHeight,
            minWeekTemp = minWeekTemperature,
            maxWeekTemp = maxWeekTemperature,
            minDayTemp = minDayTemperature,
            maxDayTemp = maxDayTemperature,
            gradientColors = gradientColors
        )
    }

    Box(
        modifier = modifier
            .cornerRadius(16.dp)
            .padding(horizontal = 8.dp)
    ) {
        Image(
            provider = ImageProvider(temperatureBarBitmap),
            contentDescription = "Temperature range bar",
            contentScale = ContentScale.FillBounds,
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(barHeight)
                .cornerRadius(16.dp)
        )
    }
}

private fun createTemperatureBarBitmap(
    context: Context,
    height: Dp,
    minWeekTemp: Double,
    maxWeekTemp: Double,
    minDayTemp: Double,
    maxDayTemp: Double,
    gradientColors: List<Color>
): Bitmap {
    val density = context.resources.displayMetrics.density

    val bitmapWidthPx = (300 * density).toInt()
    val bitmapHeightPx = (height.value * density).toInt()

    val bitmap = createBitmap(bitmapWidthPx, bitmapHeightPx)
    val canvas = Canvas(bitmap)

    val tempRange = (maxWeekTemp - minWeekTemp).toFloat()
    if (tempRange <= 0) return bitmap

    val startFraction = ((minDayTemp - minWeekTemp) / tempRange).toFloat().coerceIn(0f, 1f)
    val endFraction = ((maxDayTemp - minWeekTemp) / tempRange).toFloat().coerceIn(0f, 1f)

    val startPx = startFraction * bitmapWidthPx
    val endPx = endFraction * bitmapWidthPx

    val cornerRadius = bitmapHeightPx / 2f

    val backgroundPaint = Paint().apply {
        isAntiAlias = true
        color = Color.Black.copy(alpha = 0.2f).toArgb()
        style = Paint.Style.FILL
    }
    canvas.drawRoundRect(
        0f, 0f, bitmapWidthPx.toFloat(), bitmapHeightPx.toFloat(),
        cornerRadius, cornerRadius,
        backgroundPaint
    )

    val gradientPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        shader = LinearGradient(
            startPx, 0f,
            endPx, 0f,
            gradientColors.map { it.toArgb() }.toIntArray(),
            null,
            Shader.TileMode.CLAMP
        )
    }
    canvas.drawRoundRect(
        startPx, 0f, endPx, bitmapHeightPx.toFloat(),
        cornerRadius, cornerRadius,
        gradientPaint
    )

    return bitmap
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 400, heightDp = 400)
@Composable
private fun MediumWidgetPreview() {
    GlanceTheme {
        AnimatedMediumLargeWidget(
            weather = PreviewWeatherLocation,
            onWidgetClicked = object: Action { },
            showDays = true,
            width = 400f,
            height = 400f,
            daysCount = 5,
        )
    }
}
