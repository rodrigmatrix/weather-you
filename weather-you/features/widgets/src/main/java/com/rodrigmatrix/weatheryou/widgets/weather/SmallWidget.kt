package com.rodrigmatrix.weatheryou.widgets.weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.domain.model.WeatherIcons
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeather
import com.rodrigmatrix.weatheryou.weathericons.R

private const val MAX_WIDTH = 186

@Composable
fun SmallWidget(
    weather: WidgetWeather,
    onWidgetClicked: Action,
    glanceModifier: GlanceModifier = GlanceModifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = glanceModifier
            .background(GlanceTheme.colors.primaryContainer)
            .size(width = MAX_WIDTH.dp, height = 184.dp)
            .clickable(onClick = onWidgetClicked)
    ) {
        Spacer(GlanceModifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.Vertical.CenterVertically,
            modifier = GlanceModifier
                .width(width = MAX_WIDTH.dp)
                .padding(horizontal = 16.dp)
        ) {
            Image(
                provider = ImageProvider(weather.weatherIcons.staticIcon),
                contentDescription = null,
                modifier = GlanceModifier.size(36.dp)
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                text = weather.name.substringBefore(","),
                maxLines = 1,
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
        }
        Spacer(modifier = GlanceModifier.defaultWeight())
        Text(
            text = weather.currentWeather.temperatureString(),
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = GlanceModifier
                .width(width = MAX_WIDTH.dp)
                .padding(horizontal = 16.dp),
        )
        Row(
            modifier = GlanceModifier
                .width(width = MAX_WIDTH.dp)
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
        Spacer(GlanceModifier.height(8.dp))
    }
}

@Preview
@Composable
private fun SmallWidgetPreview() {
    SmallWidget(
        weather = PreviewWidgetWeather,
        onWidgetClicked = FakeAction,
    )
}

object FakeAction: Action

val PreviewWidgetWeather = WidgetWeather(
    id = 0,
    name = "São Paulo",
    lat = 0.0,
    long = 0.0,
    weatherIcons = WeatherIcons(
        animatedIcon = R.drawable.ic_weather_rainynight,
        staticIcon = R.drawable.ic_weather_rainynight,
    ),
    currentWeather = 20.0,
    currentCondition = "cloudy",
    maxWeather = 21.0,
    minWeather = 8.0,
    lastUpdate = "",
    hours = emptyList(),
    days = emptyList(),
)