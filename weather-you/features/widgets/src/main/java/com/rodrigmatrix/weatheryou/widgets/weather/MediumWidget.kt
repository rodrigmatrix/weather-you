package com.rodrigmatrix.weatheryou.widgets.weather

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.rodrigmatrix.weatheryou.components.extensions.getStaticIcon
import com.rodrigmatrix.weatheryou.components.extensions.getString
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeather
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeatherHour
import com.rodrigmatrix.weatheryou.widgets.R
import java.text.SimpleDateFormat
import java.util.Calendar


@Composable
fun MediumWidget(
    weather: WidgetWeather,
    onWidgetClicked: Action,
    showDays: Boolean,
    glanceModifier: GlanceModifier = GlanceModifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = glanceModifier
            .background(ImageProvider(R.drawable.weather_outside_shape_widget))
            .fillMaxSize()
            .clickable(onClick = onWidgetClicked)
    ) {
        Spacer(GlanceModifier.height(8.dp))
        MediumWidgetHeader(weather)
        Spacer(modifier = GlanceModifier.defaultWeight())
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = GlanceModifier.fillMaxWidth(),
        ) {
            CurrentConditionsContent(weather)
            Spacer(modifier = GlanceModifier.defaultWeight())
            HoursList(hoursList = weather.hours)
        }
        Spacer(GlanceModifier.height(16.dp))
        if (showDays) {
            DaysList(daysList = weather.days)
            Spacer(GlanceModifier.height(16.dp))
        }
    }
}

@Composable
private fun MediumWidgetHeader(
    weather: WidgetWeather
) {
    Row(
        verticalAlignment = Alignment.Vertical.CenterVertically,
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Image(
            provider = ImageProvider(weather.currentCondition.getStaticIcon()),
            contentDescription = null,
            modifier = GlanceModifier.size(36.dp)
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = weather.name.substringBefore(","),
                maxLines = 1,
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 16.sp,
                )
            )
            Text(
                text = LocalContext.current.getString(weather.currentCondition.getString()),
                maxLines = 1,
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }
}

@Composable
private fun CurrentConditionsContent(weather: WidgetWeather) {
    Column {
        Text(
            text = weather.currentWeather.temperatureString(),
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = GlanceModifier
                .padding(horizontal = 16.dp),
        )
        Row(
            modifier = GlanceModifier
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = weather.maxWeather.temperatureString(),
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.width(4.dp))
            Text(
                text = weather.minWeather.temperatureString(),
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 14.sp,
                )
            )
        }
    }
}

@Composable
private fun HoursList(
    hoursList: List<WidgetWeatherHour>,
    modifier: GlanceModifier = GlanceModifier,
) {
    val context = LocalContext.current
    val currentTime = Calendar.getInstance()
    val pattern = if (DateFormat.is24HourFormat(context)) {
        "HH"
    } else {
        "hh aa"
    }
    Row(modifier = modifier) {
        hoursList.forEach { weatherHour ->
            currentTime.add(Calendar.HOUR, 1)
            HourRow(
                hour = weatherHour,
                hourString = DateFormat.format(pattern, currentTime.time)
                    .toString()
                    .replace(" ", ""),
            )
            Spacer(GlanceModifier.padding(end = 8.dp))
        }
    }
}

@Composable
private fun HourRow(
    hour: WidgetWeatherHour,
    hourString: String,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = hour.weather.temperatureString(),
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Image(
            provider = ImageProvider(hour.condition.getStaticIcon()),
            contentDescription = null,
            modifier = GlanceModifier.size(36.dp)
        )
        Text(
            text = hourString,
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun DaysList(
    daysList: List<WidgetWeatherDay>,
    modifier: GlanceModifier = GlanceModifier,
) {
    Box(modifier = modifier
        .padding(
            horizontal = 16.dp,
            vertical = 8.dp,
        )
    ) {
        LazyColumn(
            modifier = modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
                .background(ImageProvider(R.drawable.weather_inside_shape_widget))
        ) {
            itemsIndexed(daysList) { index, weatherDay ->
                DayRow(
                    day = weatherDay,
                    index = index,
                )
            }
        }
    }
}

@Composable
private fun DayRow(
    day: WidgetWeatherDay,
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
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 18.sp,
            )
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        Image(
            provider = ImageProvider(day.condition.getStaticIcon()),
            contentDescription = null,
            modifier = GlanceModifier.size(36.dp)
        )
        Spacer(modifier = GlanceModifier.width(8.dp))
        Text(
            text = day.maxWeather.temperatureString(),
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = GlanceModifier.width(8.dp))
        Text(
            text = day.minWeather.temperatureString(),
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun MediumWidgetLoading(
    onWidgetClicked: Action,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(ImageProvider(R.drawable.weather_outside_shape_widget))
            .fillMaxSize()
            .clickable(onClick = onWidgetClicked)
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

@Preview
@Composable
private fun MediumWidgetPreview() {
    MediumWidget(
        weather = PreviewWidgetWeather,
        onWidgetClicked = FakeAction,
        showDays = true,
    )
}