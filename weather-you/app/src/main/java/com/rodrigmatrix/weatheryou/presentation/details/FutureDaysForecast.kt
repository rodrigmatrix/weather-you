package com.rodrigmatrix.weatheryou.presentation.details

import androidx.compose.foundation.clickable
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
import com.airbnb.lottie.compose.*
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.domain.model.Day
import com.rodrigmatrix.weatheryou.presentation.components.WeatherYouDivider
import com.rodrigmatrix.weatheryou.presentation.extensions.temperatureString

@Composable
fun FutureDaysForecast(
    futureDaysList: List<Day>,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(Modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.next_15_days_forecast),
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 10.dp,
                        top = 10.dp
                    ),
                style = MaterialTheme.typography.headlineSmall
            )
            futureDaysList.forEach { item ->
                DayRow(item)
            }
        }
    }
}

@Composable
fun DayRow(day: Day) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(day.icon))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

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
                text = day.dateTime,
                modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = day.weatherCondition,
                modifier = Modifier
                    .padding(bottom = 10.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            LottieAnimation(
                composition,
                progress,
                modifier = Modifier
                    .size(42.dp)
            )
            Column {
                Text(
                    text = day.maxTemperature.temperatureString(),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = day.minTemperature.temperatureString(),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
    WeatherYouDivider(Modifier.height(1.dp))
}
