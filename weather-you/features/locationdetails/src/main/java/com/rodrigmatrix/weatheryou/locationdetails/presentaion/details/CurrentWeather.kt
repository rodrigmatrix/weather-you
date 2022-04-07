package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.core.extensions.*
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.locationdetails.R
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.preview.PreviewWeatherLocation
import java.util.*

@Composable
fun CurrentWeather(
    weatherLocation: WeatherLocation,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    WeatherYouCard(modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 10.dp,
                    bottom = 10.dp
                )
        ) {
            Column(Modifier.weight(1f)) {
                Row {
                    Text(
                        text = stringResource(
                            R.string.day_x,
                            weatherLocation.maxTemperature.temperatureString()
                        ) + " ",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = stringResource(
                            R.string.night_x,
                            weatherLocation.lowestTemperature.temperatureString()
                        ),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Text(
                    text = weatherLocation.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = weatherLocation.timeZone.getTimeZoneHourAndMinutes(context),
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = weatherLocation.currentWeather.temperatureString(),
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 80.sp
                )
                Text(
                    text = stringResource(
                        R.string.feels_like_x,
                        weatherLocation.feelsLike.temperatureString()
                    ),
                    style = MaterialTheme.typography.titleSmall
                )
                if (weatherLocation.precipitationType.isNotEmpty()) {
                    Text(
                        text = stringResource(
                            R.string.chance_of_precipitation,
                            weatherLocation.precipitationProbability.percentageString()
                        ),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxHeight()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    WeatherIcon(
                        weatherIcons = weatherLocation.weatherIcons,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .size(100.dp)
                    )
                    Text(
                        text = weatherLocation.currentWeatherDescription.capitalize(Locale.getDefault()),
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (weatherLocation.isCurrentLocation) {
                    Icon(
                        painter = painterResource(com.rodrigmatrix.weatheryou.components.R.drawable.ic_my_location),
                        contentDescription = stringResource(R.string.current_location),
                        modifier = Modifier.align(Alignment.TopEnd)
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
    MaterialTheme {
        CurrentWeather(
            weatherLocation = PreviewWeatherLocation
        )
    }
}