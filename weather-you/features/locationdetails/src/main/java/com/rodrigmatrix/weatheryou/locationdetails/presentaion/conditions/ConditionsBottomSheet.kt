package com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions

import android.app.Activity
import android.os.Build
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.rodrigmatrix.weatheryou.components.WeatherIcon
import com.rodrigmatrix.weatheryou.components.extensions.toGradientList
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.core.extensions.getDayNumberString
import com.rodrigmatrix.weatheryou.core.extensions.getDayString
import com.rodrigmatrix.weatheryou.core.extensions.getFullDate
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.core.extensions.toTemperatureString
import com.rodrigmatrix.weatheryou.core.state.WeatherYouAppState
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.PopupValue
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.extensions.getTemperatureGradient
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import com.rodrigmatrix.weatheryou.components.theme.rain
import com.rodrigmatrix.weatheryou.components.theme.snow
import com.rodrigmatrix.weatheryou.core.extensions.getDateTimeFromTimezone
import com.rodrigmatrix.weatheryou.core.extensions.getFullDayString
import com.rodrigmatrix.weatheryou.core.extensions.getHourString
import com.rodrigmatrix.weatheryou.core.extensions.getLocalTime
import com.rodrigmatrix.weatheryou.core.extensions.percentageString
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationType
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import org.joda.time.Minutes
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConditionsBottomSheet(
    viewState: ConditionsViewState,
    bottomSheetState: SheetState,
    onDismissRequest: () -> Unit,
    onClick: (WeatherDay) -> Unit,
    onTemperatureTypeChange: (TemperatureType) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        dragHandle = { },
        sheetState = bottomSheetState,
        tonalElevation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            16.dp
        } else {
            0.dp
        },
        containerColor = WeatherYouTheme.colorScheme.background.copy(
            alpha = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                 0.3f
            } else {
                1f
            },
        ),
    ) {
        ConditionsContent(
            viewState = viewState,
            onClick = onClick,
            onTemperatureTypeChange = onTemperatureTypeChange,
            onCloseClicked = onDismissRequest,
            modifier = modifier,
        )
    }
}

@Composable
fun ConditionsContent(
    viewState: ConditionsViewState,
    onClick: (WeatherDay) -> Unit,
    onTemperatureTypeChange: (TemperatureType) -> Unit,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val weatherLocation = viewState.weatherLocation ?: return
    val day = viewState.day ?: return
    Column(
        modifier
            .fillMaxHeight(0.92f)
            .verticalScroll(rememberScrollState())
    ) {
        Surface(
            color = WeatherYouTheme.colorScheme.surface,
            onClick = onCloseClicked,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp, end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                tint = WeatherYouTheme.colorScheme.onSurface,
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
            )
        }
        Spacer(Modifier.height(20.dp))
        DaysSelector(
            day = day,
            days = weatherLocation.days,
            onClick = onClick,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(20.dp))
        HorizontalDivider(
            color = WeatherYouTheme.colorScheme.onBackground.copy(alpha = 0.12f)
        )
        Spacer(Modifier.height(10.dp))

        when (viewState.type) {
            ConditionType.Conditions -> ConditionsContent(
                weatherLocation = weatherLocation,
                day = day,
                viewState = viewState,
                onTemperatureTypeChange = onTemperatureTypeChange,
            )
            ConditionType.SunriseSunset -> Unit
        }

        Spacer(Modifier.height(200.dp))
    }
}

@Composable
fun DaysSelector(
    day: WeatherDay,
    days: List<WeatherDay>,
    onClick: (WeatherDay) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        LazyRow {
            items(days) { item ->
                DayPickItem(
                    day = item,
                    isSelected = item == day,
                    onClick = onClick,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Crossfade(
            targetState = day.dateTime.getFullDate(),
            label = "",
        ) { value ->
            Text(
                text = value,
                style = WeatherYouTheme.typography.titleLarge,
                color = WeatherYouTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
fun DayPickItem(
    day: WeatherDay,
    isSelected: Boolean,
    onClick: (WeatherDay) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = day.dateTime.getDayString().first().toString(),
            style = WeatherYouTheme.typography.bodyMedium,
            color = WeatherYouTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = modifier
                .clickable {
                    onClick(day)
                }
                .background(
                    if (isSelected) {
                        WeatherYouTheme.colorScheme.primary
                    } else {
                        WeatherYouTheme.colorScheme.surface
                    },
                    CircleShape
                )
                .size(30.dp)
                .padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = day.dateTime.getDayNumberString(),
                style = WeatherYouTheme.typography.bodyMedium,
                color = if (isSelected) {
                    WeatherYouTheme.colorScheme.onPrimary
                } else {
                    WeatherYouTheme.colorScheme.onSurface
                },
            )
        }
    }
}