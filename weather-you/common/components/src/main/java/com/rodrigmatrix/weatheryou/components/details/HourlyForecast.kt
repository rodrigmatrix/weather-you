package com.rodrigmatrix.weatheryou.components.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.WeatherYouDivider
import com.rodrigmatrix.weatheryou.components.theme.weatherTextColor
import com.rodrigmatrix.weatheryou.core.extensions.*
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationType
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
            color = WeatherYouTheme.colorScheme.weatherTextColor,
        )
        WeatherYouDivider(
            Modifier
                .padding(bottom = 10.dp)
                .height(1.dp)
        )
        LazyRow(Modifier.padding(start = 16.dp, end = 16.dp)) {
            itemsIndexed(hoursList) { index, item  ->
                hourItemWrapper {
                    HourRow(item, index == 0)
                }
            }
        }
    }
}

@Composable
fun HourRow(hour: WeatherHour, firstItem: Boolean) {
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
            color = WeatherYouTheme.colorScheme.weatherTextColor,
            style = WeatherYouTheme.typography.bodySmall,
        )
        if (hour.precipitationType != PrecipitationType.Clear) {
            Text(
                text = hour.precipitationProbability.percentageString(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 4.dp),
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                style = WeatherYouTheme.typography.bodySmall,
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
            isDaylight = hour.isDaylight,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
                .size(34.dp)
        )
        Text(
            text = if (firstItem) {
                stringResource(R.string.now)
            } else {
                hour.dateTime.getHourString(LocalContext.current)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = WeatherYouTheme.colorScheme.weatherTextColor,
            style = WeatherYouTheme.typography.bodySmall
        )
    }
}
