package com.rodrigmatrix.weatheryou.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme.weatherTextColor
import com.rodrigmatrix.weatheryou.core.extensions.getHourWithMinutesString
import com.rodrigmatrix.weatheryou.core.extensions.getTimeZoneHourAndMinutes
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

@Composable
fun WeatherLocationCardContent(
    weatherLocation: WeatherLocation,
    isRefreshingLocations: Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .fillMaxHeight()
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = if (isRefreshingLocations) {
                    "-----"
                } else {
                    weatherLocation.currentWeather.temperatureString()
                },
                style = WeatherYouTheme.typography.headlineLarge,
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = weatherLocation.timeZone.getTimeZoneHourAndMinutes(context).ifEmpty {
                    weatherLocation.currentTime.getHourWithMinutesString(context)
                },
                style = WeatherYouTheme.typography.bodyMedium,
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = weatherLocation.name,
                style = WeatherYouTheme.typography.headlineMedium,
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .height(100.dp),
        ) {
            WeatherIcon(
                weatherCondition = weatherLocation.currentCondition,
                isDaylight = weatherLocation.isDaylight,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center)
            )
            if (weatherLocation.isCurrentLocation) {
                Icon(
                    painter = painterResource(R.drawable.ic_my_location),
                    tint = WeatherYouTheme.colorScheme.onSecondaryContainer,
                    contentDescription = stringResource(R.string.current_location),
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    }
}
