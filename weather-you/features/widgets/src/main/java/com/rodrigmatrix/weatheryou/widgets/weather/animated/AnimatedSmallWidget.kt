package com.rodrigmatrix.weatheryou.widgets.weather.animated

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
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
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherLocation
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.widgets.weather.utils.gradientBackground

private const val MAX_WIDTH = 186

@Composable
fun AnimatedSmallWidget(
    weather: WeatherLocation,
    width: Float,
    height: Float,
    onWidgetClicked: Action,
    glanceModifier: GlanceModifier = GlanceModifier,
) {
    Column(
        modifier = glanceModifier
            .background(ImageProvider(weather.gradientBackground(width, height)))
            .size(width = MAX_WIDTH.dp, height = 184.dp)
            .padding(horizontal = 16.dp)
            .clickable(onWidgetClicked)
    ) {
        Spacer(GlanceModifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = weather.name.substringBefore(","),
                maxLines = 1,
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 16.sp,
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
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold
            ),
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        Image(
            provider = ImageProvider(weather.currentCondition.getStaticIcon(weather.isDaylight)),
            contentDescription = null,
            modifier = GlanceModifier.size(24.dp)
        )
        Text(
            text = LocalContext.current.getString((weather.currentCondition.getString(weather.isDaylight))),
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 16.sp,
            ),
        )
        Row(
            modifier = GlanceModifier
        ) {
            Text(
                text = weather.maxTemperature.temperatureString(),
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 20.sp,
                )
            )
            Spacer(modifier = GlanceModifier.width(4.dp))
            Text(
                text = weather.lowestTemperature.temperatureString(),
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 14.sp,
                )
            )
        }
        Spacer(GlanceModifier.height(8.dp))
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 200, heightDp = 200)
@Composable
private fun SmallWidgetPreview() {
    GlanceTheme {
        AnimatedSmallWidget(
            weather = PreviewWeatherLocation,
            onWidgetClicked = object: Action { },
            width = 200f,
            height = 200f,
        )
    }
}