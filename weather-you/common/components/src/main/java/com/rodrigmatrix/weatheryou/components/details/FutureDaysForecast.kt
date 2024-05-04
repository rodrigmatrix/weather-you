package com.rodrigmatrix.weatheryou.components.details

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme

private const val TODAY_INDEX = 0
private const val TOMORROW_INDEX = 1

@Composable
fun FutureDaysForecast(
    futureDaysList: List<WeatherDay>,
    isExpanded: Boolean,
    onExpandedButtonClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    WeatherYouCard(modifier = modifier) {
        FutureDaysForecastContent(
            futureDaysList = futureDaysList,
            isExpanded = isExpanded,
            onExpandedButtonClick = onExpandedButtonClick,
        )
    }
}

@Composable
fun FutureDaysForecastContent(
    futureDaysList: List<WeatherDay>,
    isExpanded: Boolean,
    onExpandedButtonClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
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
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            ExpandButton(
                isExpanded = isExpanded,
                contentDescription = stringResource(R.string.show_all_days_forecast),
                onExpandButtonClick = {
                    onExpandedButtonClick(it)
                },
            )
        }
        futureDaysList.forEachIndexed { index, day ->
            DayRow(day, index)
        }
    }
}

@Composable
fun DayRow(day: WeatherDay, index: Int) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    WeatherYouDivider(Modifier.height(1.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isExpanded = !isExpanded
            }
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = when (index) {
                    TODAY_INDEX -> stringResource(R.string.today)
                    TOMORROW_INDEX -> stringResource(R.string.tomorrow)
                    else -> day.dateTime.getDateWithMonth()
                },
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(id = day.weatherCondition.getString()),
                modifier = Modifier.padding(bottom = 10.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row {
            Column {
                WeatherIcon(
                    weatherCondition = day.weatherCondition,
                    modifier = Modifier.size(42.dp)
                )
                if (day.precipitationType.isNotEmpty()) {
                    Text(
                        text = day.precipitationProbability.percentageString(),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Column(Modifier.align(Alignment.Top)) {
                Text(
                    text = day.maxTemperature.temperatureString(),
                    modifier = Modifier.align(Alignment.End),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = day.minTemperature.temperatureString(),
                    modifier = Modifier.align(Alignment.End),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
    ExpandedCardContent(
        day = day,
        isExpanded = isExpanded
    )
}

@Composable
fun ExpandedCardContent(
    day: WeatherDay,
    isExpanded: Boolean
) {
    val context = LocalContext.current
    AnimatedVisibility(visible = isExpanded) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            LazyRow(Modifier.padding(start = 16.dp, end = 16.dp)) {
                items(day.hours) { item  ->
                    HourRow(item)
                }
            }
            if (day.precipitationType.isNotEmpty()) {
                Text(
                    text = stringResource(
                        R.string.chance_of_precipitation,
                        day.precipitationProbability.percentageString()
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
            Text(
                text = stringResource(R.string.sunrise_x, day.sunrise.getHourWithMinutesString(context)),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = stringResource(R.string.sunset_x, day.sunset.getHourWithMinutesString(context)),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = stringResource(R.string.wind_x, day.windSpeed.speedString()),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = stringResource(R.string.humidity_x, day.humidity.percentageString()),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }
    }
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FutureDaysForecastPreview() {
    MaterialTheme {
        FutureDaysForecast(
            PreviewFutureDaysForecast,
            isExpanded = false,
            {}
        )
    }
}

