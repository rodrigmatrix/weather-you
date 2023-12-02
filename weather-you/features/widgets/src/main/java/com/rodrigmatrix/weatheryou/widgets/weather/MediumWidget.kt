package com.rodrigmatrix.weatheryou.widgets.weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeather
import com.rodrigmatrix.weatheryou.weathericons.R


@Composable
fun MediumWidget(
    weather: WidgetWeather,
    onWidgetClicked: Action,
    glanceModifier: GlanceModifier = GlanceModifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = glanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.primary)
            .clickable(onClick = onWidgetClicked)
    ) {
        Text(
            text = weather.currentWeather.temperatureString(),
            style = TextStyle(
                color = ColorProvider(Color.White), fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = GlanceModifier.height(20.dp))
        Image(
            provider = ImageProvider(R.drawable.ic_weather_rainynight),
            contentDescription = null,
            modifier = GlanceModifier.size(40.dp)
        )
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