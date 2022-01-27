package com.rodrigmatrix.weatheryou.presentation.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.extensions.percentageString
import com.rodrigmatrix.weatheryou.presentation.extensions.temperatureString

@Composable
fun CurrentWeather(
    weatherLocation: WeatherLocation,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(weatherLocation.currentWeatherIcon)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 10.dp,
                    bottom = 10.dp
                )
        ) {
            Column {
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
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = weatherLocation.currentTime,
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
                            weatherLocation.precipitationType,
                            weatherLocation.precipitationProbability.percentageString()
                        ),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LottieAnimation(
                    composition,
                    progress,
                    modifier = Modifier
                        .size(100.dp)
                )
                Text(
                    text = weatherLocation.currentWeatherDescription,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}