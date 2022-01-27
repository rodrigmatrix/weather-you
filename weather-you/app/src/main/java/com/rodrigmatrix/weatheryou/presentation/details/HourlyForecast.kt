package com.rodrigmatrix.weatheryou.presentation.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.presentation.components.WeatherYouDivider
import com.rodrigmatrix.weatheryou.presentation.extensions.getHourString
import com.rodrigmatrix.weatheryou.presentation.extensions.temperatureString
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewHourlyForecast

private const val FIRST_INDEX = 0

@Composable
fun HourlyForecast(
    hoursList: List<WeatherHour>,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column {
            Text(
                text = stringResource(R.string.daily_forecast),
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 10.dp,
                        top = 10.dp
                    ),
                style = MaterialTheme.typography.headlineSmall
            )
            WeatherYouDivider(
                Modifier
                    .padding(bottom = 10.dp)
                    .height(1.dp)
            )
            LazyRow(Modifier.padding(start = 16.dp, end = 16.dp)) {
                itemsIndexed(hoursList) { index, item  ->
                    HourRow(item, index == FIRST_INDEX)
                }
            }
        }
    }

}

@Composable
fun HourRow(hour: WeatherHour, isCurrentHour: Boolean) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(hour.icon))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Column(
        Modifier.padding(
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
                .padding(bottom = 10.dp),
            style = MaterialTheme.typography.bodySmall
        )
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
                .size(34.dp)
        )
        Text(
            text = if (isCurrentHour) {
                stringResource(R.string.now)
            } else {
                hour.dateTime.getHourString()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeatherLocationPreview() {
    WeatherYouTheme {
        HourlyForecast(PreviewHourlyForecast)
    }
}

