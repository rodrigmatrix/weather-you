package com.rodrigmatrix.weatheryou.components.details

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.ExpandButton
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.WeatherYouDivider
import com.rodrigmatrix.weatheryou.core.extensions.*
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.extensions.getString
import com.rodrigmatrix.weatheryou.components.preview.PreviewFutureDaysForecast
import com.rodrigmatrix.weatheryou.components.temperature.TemperatureBar
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme.weatherTextColor

private const val TODAY_INDEX = 0
private const val TOMORROW_INDEX = 1

@Composable
fun FutureDaysForecast(
    futureDaysList: List<WeatherDay>,
    minWeekTemperature: Double,
    maxWeekTemperature: Double,
    currentTemperature: Double,
    isExpanded: Boolean,
    onExpandedButtonClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    WeatherYouCard(modifier = modifier) {
        FutureDaysForecastContent(
            futureDaysList = futureDaysList,
            minWeekTemperature = minWeekTemperature,
            maxWeekTemperature = maxWeekTemperature,
            currentTemperature = currentTemperature,
            isExpanded = isExpanded,
            onExpandedButtonClick = onExpandedButtonClick,
        )
    }
}

@Composable
fun FutureDaysForecastContent(
    futureDaysList: List<WeatherDay>,
    minWeekTemperature: Double,
    maxWeekTemperature: Double,
    currentTemperature: Double,
    isExpanded: Boolean,
    onExpandedButtonClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    dayItem: @Composable (index: Int, day: WeatherDay) -> Unit = { index, day ->
        DayRow(
            minWeekTemperature = minWeekTemperature,
            maxWeekTemperature = maxWeekTemperature,
            index = index,
            currentTemperature = currentTemperature,
            day = day,
        )
    },
    expandButton: @Composable () -> Unit = {
        ExpandButton(
            isExpanded = isExpanded,
            contentDescription = stringResource(R.string.show_all_days_forecast),
            onExpandButtonClick = {
                onExpandedButtonClick(it)
            },
        )
    },
) {
    Column(modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 10.dp,
                    horizontal = 16.dp,
                )
        ) {
            Text(
                text = stringResource(R.string.next_x_days_forecast, futureDaysList.size),
                style = WeatherYouTheme.typography.headlineSmall,
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            expandButton()
        }
        futureDaysList.forEachIndexed { index, day ->
            dayItem(index, day)
        }
    }
}

@Composable
fun DayRow(
    minWeekTemperature: Double,
    maxWeekTemperature: Double,
    currentTemperature: Double,
    day: WeatherDay,
    index: Int,
) {
    var isExpanded by remember(day) { mutableStateOf(false) }
    WeatherYouDivider(Modifier.height(1.dp))
    DayContent(
        minWeekTemperature = minWeekTemperature,
        maxWeekTemperature = maxWeekTemperature,
        day = day,
        currentTemperature = currentTemperature,
        index = index,
        modifier = Modifier.clickable {
            isExpanded = !isExpanded
        }
    )
    ExpandedCardContent(
        day = day,
        isExpanded = isExpanded
    )
}

@Composable
fun DayContent(
    minWeekTemperature: Double,
    maxWeekTemperature: Double,
    currentTemperature: Double,
    day: WeatherDay,
    index: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f, true)
        ) {
            Text(
                text = when (index) {
                    TODAY_INDEX -> stringResource(R.string.today)
                    TOMORROW_INDEX -> stringResource(R.string.tomorrow)
                    else -> day.dateTime.getDayString()
                },
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                style = WeatherYouTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(id = day.weatherCondition.getString(isDaylight = true)),
                modifier = Modifier.padding(bottom = 10.dp),
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                style = WeatherYouTheme.typography.bodyMedium
            )
        }
        TemperatureBar(
            minWeekTemperature = minWeekTemperature,
            maxWeekTemperature = maxWeekTemperature,
            minDayTemperature = day.minTemperature,
            maxDayTemperature = day.maxTemperature,
            hours = day.hours,
            currentTemperature = if (index == 0) {
                currentTemperature
            } else {
                null
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(2f, true),
        )
        Row {
            Column {
                WeatherIcon(
                    weatherCondition = day.weatherCondition,
                    isDaylight = true,
                    modifier = Modifier.size(42.dp)
                )
                if (day.precipitationProbability >= 30.0) {
                    Text(
                        text = day.precipitationProbability.percentageString(),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = WeatherYouTheme.colorScheme.weatherTextColor,
                        style = WeatherYouTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }
            Column(Modifier.align(Alignment.Top)) {
                Text(
                    text = day.maxTemperature.temperatureString(),
                    modifier = Modifier.align(Alignment.End),
                    color = WeatherYouTheme.colorScheme.weatherTextColor,
                    style = WeatherYouTheme.typography.bodyMedium
                )
                Text(
                    text = day.minTemperature.temperatureString(),
                    modifier = Modifier.align(Alignment.End),
                    color = WeatherYouTheme.colorScheme.weatherTextColor,
                    style = WeatherYouTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ExpandedCardContent(
    day: WeatherDay,
    isExpanded: Boolean,
    hourItemWrapper: @Composable (@Composable () -> Unit) -> Unit = { content ->
        content()
    },
) {
    val context = LocalContext.current
    AnimatedVisibility(visible = isExpanded) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            LazyRow(Modifier.padding(start = 16.dp, end = 16.dp)) {
                items(day.hours) { item  ->
                    hourItemWrapper {
                        HourRow(item, false)
                    }
                }
            }
            if (day.precipitationProbability >= 30.0) {
                Text(
                    text = stringResource(
                        R.string.chance_of_precipitation,
                        day.precipitationProbability.percentageString()
                    ),
                    style = WeatherYouTheme.typography.titleSmall,
                    color = WeatherYouTheme.colorScheme.weatherTextColor,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
            Text(
                text = stringResource(R.string.sunrise_x, day.sunrise.getHourWithMinutesString(context)),
                style = WeatherYouTheme.typography.titleSmall,
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = stringResource(R.string.sunset_x, day.sunset.getHourWithMinutesString(context)),
                style = WeatherYouTheme.typography.titleSmall,
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = stringResource(R.string.wind_x, day.windSpeed.speedString()),
                style = WeatherYouTheme.typography.titleSmall,
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = stringResource(R.string.humidity_x, day.humidity.percentageString()),
                style = WeatherYouTheme.typography.titleSmall,
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }
    }
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FutureDaysForecastPreview() {
    WeatherYouTheme {
        FutureDaysForecast(
            PreviewFutureDaysForecast,
            maxWeekTemperature = 20.0,
            minWeekTemperature = 10.0,
            currentTemperature = 20.0,
            isExpanded = false,
            onExpandedButtonClick = {}
        )
    }
}

