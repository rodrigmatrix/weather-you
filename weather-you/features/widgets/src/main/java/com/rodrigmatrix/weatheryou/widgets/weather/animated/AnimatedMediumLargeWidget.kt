package com.rodrigmatrix.weatheryou.widgets.weather.animated

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
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
import kotlin.math.absoluteValue

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
            itemsIndexed(daysList.take(daysCount)) { index, weatherDay ->
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
            text = DateFormat.format("EEEE", calendar.time).toString(),
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 18.sp,
            )
        )

        TemperatureGlanceBar(
            minWeekTemperature = minWeekTemperature,
            maxWeekTemperature = maxWeekTemperature,
            minDayTemperature = day.minTemperature,
            maxDayTemperature = day.maxTemperature,
            modifier = GlanceModifier.defaultWeight()
        )
        Image(
            provider = ImageProvider(day.weatherCondition.getStaticIcon(isDaylight = true)),
            contentDescription = null,
            modifier = GlanceModifier.size(24.dp)
        )
        Text(
            text = day.maxTemperature.temperatureString(),
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer(modifier = GlanceModifier.width(8.dp))
        Text(
            text = day.minTemperature.temperatureString(),
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        )
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
    modifier: GlanceModifier = GlanceModifier,
) {
    Box(
        modifier
            .cornerRadius(16.dp)
            .padding(horizontal = 8.dp)
    ) {

        val gradientList = getTemperatureGradient(
            minDayTemperature = minDayTemperature,
            maxDayTemperature = maxDayTemperature,
            hours = emptyList(),
        ).map { it.toArgb() }
        val width = 290f
        val height = 8f
        val valueIncrease = ((width / (maxWeekTemperature - minWeekTemperature))).toFloat()
        val startPosition = valueIncrease * (minWeekTemperature - minDayTemperature).absoluteValue
        val endPosition = ((width / (maxWeekTemperature - minWeekTemperature)) * (maxDayTemperature - minWeekTemperature))
        val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val backgroundColor = Color.Black.copy(alpha = 0.1f)
        val backgroundPaint = Paint().apply {
            strokeWidth = 20f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            color = backgroundColor.toArgb()
        }
        val gradientPoint = Paint().apply {
            strokeWidth = height
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            shader = LinearGradient(startPosition.toFloat(), height, width, height, gradientList.toIntArray(), null, Shader.TileMode.MIRROR)
        }
        canvas.apply {
            drawLine(0f, height, width, height, backgroundPaint)
            drawLine(startPosition.toFloat(), 4f, endPosition.toFloat(), 4f, gradientPoint)
        }
        Image(
            modifier = GlanceModifier
                .cornerRadius(16.dp)
                .height(height.dp)
                .width(width.dp),
            provider = ImageProvider(bitmap),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
        )
    }
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