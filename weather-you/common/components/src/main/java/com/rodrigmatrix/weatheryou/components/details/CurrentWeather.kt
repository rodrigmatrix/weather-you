package com.rodrigmatrix.weatheryou.components.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.TextAutoSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.extensions.getString
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherLocation
import com.rodrigmatrix.weatheryou.components.theme.weatherTextColor
import com.rodrigmatrix.weatheryou.core.extensions.getHourWithMinutesString
import com.rodrigmatrix.weatheryou.core.extensions.getTimeZoneHourAndMinutes
import com.rodrigmatrix.weatheryou.core.extensions.percentageString
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationType
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

@Composable
fun CurrentWeatherContent(
    weatherLocation: WeatherLocation,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp,
            )
    ) {
        Row(Modifier.fillMaxWidth()) {
            Row(Modifier.weight(1f)) {
                Text(
                    text = stringResource(
                        R.string.day_x,
                        weatherLocation.maxTemperature.temperatureString()
                    ) + " ",
                    color = WeatherYouTheme.colorScheme.weatherTextColor,
                    style = WeatherYouTheme.typography.titleSmall
                )
                Text(
                    text = stringResource(
                        R.string.night_x,
                        weatherLocation.lowestTemperature.temperatureString()
                    ),
                    color = WeatherYouTheme.colorScheme.weatherTextColor,
                    style = WeatherYouTheme.typography.titleSmall
                )
            }

            if (weatherLocation.isCurrentLocation) {
                Icon(
                    painter = painterResource(R.drawable.ic_my_location),
                    tint = WeatherYouTheme.colorScheme.onSecondaryContainer,
                    contentDescription = stringResource(R.string.current_location),
                    modifier = Modifier
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = weatherLocation.name,
                    style = WeatherYouTheme.typography.titleMedium,
                    maxLines = 2,
                    color = WeatherYouTheme.colorScheme.weatherTextColor,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = weatherLocation.timeZone.getTimeZoneHourAndMinutes(context).ifEmpty {
                        weatherLocation.currentTime.getHourWithMinutesString(context)
                    },
                    color = WeatherYouTheme.colorScheme.weatherTextColor,
                    style = WeatherYouTheme.typography.titleSmall
                )
                Text(
                    text = weatherLocation.currentWeather.temperatureString(),
                    color = WeatherYouTheme.colorScheme.weatherTextColor,
                    style = WeatherYouTheme.typography.headlineLarge,
                    autoSize = TextAutoSize.StepBased(minFontSize = 10.sp, maxFontSize = 70.sp, stepSize = 10.sp)
                )
                Text(
                    text = stringResource(
                        R.string.feels_like_x,
                        weatherLocation.feelsLike.temperatureString()
                    ),
                    color = WeatherYouTheme.colorScheme.weatherTextColor,
                    style = WeatherYouTheme.typography.titleSmall
                )
                if (weatherLocation.precipitationType != PrecipitationType.Clear) {
                    Text(
                        text = stringResource(
                            R.string.chance_of_precipitation,
                            weatherLocation.precipitationProbability.percentageString()
                        ),
                        color = WeatherYouTheme.colorScheme.weatherTextColor,
                        style = WeatherYouTheme.typography.titleSmall
                    )
                }
            }
            Box {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    WeatherIcon(
                        weatherCondition = weatherLocation.currentCondition,
                        isDaylight = weatherLocation.isDaylight,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .size(100.dp)
                    )
                    Text(
                        text = stringResource(id = weatherLocation.currentCondition.getString(weatherLocation.isDaylight)),
                        style = WeatherYouTheme.typography.titleSmall,
                        maxLines = 2,
                        color = WeatherYouTheme.colorScheme.weatherTextColor,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CurrentWeatherPreview() {
    WeatherYouTheme {
        CurrentWeatherContent(
            weatherLocation = PreviewWeatherLocation
        )
    }
}