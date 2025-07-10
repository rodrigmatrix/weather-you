package com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.core.extensions.getDateTimeFromTimezone
import com.rodrigmatrix.weatheryou.core.extensions.getLocalTime
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import org.joda.time.Minutes

@Composable
fun CurrentDayGraphBox(
    weatherLocation: WeatherLocation,
    day: WeatherDay,
    modifier: Modifier = Modifier,
    chart: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        chart()
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
}