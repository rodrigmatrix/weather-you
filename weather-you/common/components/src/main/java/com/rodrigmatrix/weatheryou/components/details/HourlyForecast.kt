package com.rodrigmatrix.weatheryou.components.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.WeatherYouDivider
import com.rodrigmatrix.weatheryou.core.extensions.*
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour

@Composable
fun HourlyForecastContent(
    hoursList: List<WeatherHour>,
    modifier: Modifier = Modifier,
    hourItemWrapper: @Composable (content: @Composable () -> Unit) -> Unit = { content ->
        content()
    },
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.daily_forecast),
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 10.dp,
                    top = 10.dp
                ),
            style = WeatherYouTheme.typography.headlineSmall,
            color = WeatherYouTheme.colorScheme.onSecondaryContainer,
        )
        WeatherYouDivider(
            Modifier
                .padding(bottom = 10.dp)
                .height(1.dp)
        )
        LazyRow(Modifier.padding(start = 16.dp, end = 16.dp)) {
            items(hoursList) { item  ->
                hourItemWrapper {
                    HourRow(item)
                }
            }
        }
    }
}

@Composable
fun HourRow(hour: WeatherHour) {
    Column(
        Modifier
            .padding(
                top = 10.dp,
                bottom = 10.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {
        Text(
            text = hour.temperature.temperatureString(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 4.dp),
            color = WeatherYouTheme.colorScheme.onSecondaryContainer,
            style = WeatherYouTheme.typography.bodySmall
        )
        if (hour.precipitationType.isNotEmpty()) {
            Text(
                text = hour.precipitationProbability.percentageString(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 4.dp),
                color = WeatherYouTheme.colorScheme.onSecondaryContainer,
                style = WeatherYouTheme.typography.bodySmall
            )
        } else {
            Spacer(
                Modifier
                    .padding(bottom = 4.dp)
                    .height(16.dp)
            )
        }
        WeatherIcon(
            weatherCondition = hour.weatherCondition,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
                .size(34.dp)
        )
        Text(
            text = hour.dateTime.getHourString(LocalContext.current),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = WeatherYouTheme.colorScheme.onSecondaryContainer,
            style = WeatherYouTheme.typography.bodySmall
        )
    }
}
