package com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions

import android.os.Build
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.core.extensions.getDayNumberString
import com.rodrigmatrix.weatheryou.core.extensions.getDayString
import com.rodrigmatrix.weatheryou.core.extensions.getFullDate
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.R as StringR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConditionsBottomSheet(
    viewState: ConditionsViewState,
    bottomSheetState: SheetState,
    scrollState: ScrollState,
    onDismissRequest: () -> Unit,
    onTypeChange: (ConditionType) -> Unit,
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
            scrollState = scrollState,
            onClick = onClick,
            onTemperatureTypeChange = onTemperatureTypeChange,
            onCloseClicked = onDismissRequest,
            onTypeChange = onTypeChange,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ConditionsContent(
    viewState: ConditionsViewState,
    scrollState: ScrollState,
    onClick: (WeatherDay) -> Unit,
    onTypeChange: (ConditionType) -> Unit,
    onTemperatureTypeChange: (TemperatureType) -> Unit,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val weatherLocation = viewState.weatherLocation ?: return
    val day = viewState.day ?: return

    val flingBehavior = remember<FlingBehavior> {
        object : FlingBehavior {
            override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
                return 0f
            }
        }
    }
    var expandedMenu by remember { mutableStateOf(false) }
    var popupExpanded by remember { mutableStateOf(false) }
    val menuIconRotation by animateFloatAsState(
        targetValue = if (expandedMenu) 180f else 0f,
        label = "",
    )

    Column(
        modifier
            .fillMaxHeight(0.92f)
            .verticalScroll(scrollState, flingBehavior = flingBehavior)
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

        Box(Modifier.fillMaxWidth()) {
            when (viewState.type) {
                ConditionType.Conditions -> ConditionsContent(
                    weatherLocation = weatherLocation,
                    day = day,
                    viewState = viewState,
                    onPopupStateChange = {
                        popupExpanded = it
                    },
                    onTemperatureTypeChange = onTemperatureTypeChange,
                )
                ConditionType.UvIndex -> UvIndexContent(
                    day = day,
                    viewState = viewState,
                    onPopupStateChange = {
                        popupExpanded = it
                    },
                    weatherLocation = weatherLocation,
                )
                ConditionType.Wind -> WindContent(
                    day = day,
                    viewState = viewState,
                    onPopupStateChange = {
                        popupExpanded = it
                    },
                    weatherLocation = weatherLocation,
                )
                ConditionType.Precipitation -> PrecipitationContent(
                    day = viewState.day,
                    onPopupStateChange = {
                        popupExpanded = it
                    },
                )
                ConditionType.Humidity -> HumidityContent(
                    day = day,
                    viewState = viewState,
                    onPopupStateChange = {
                        popupExpanded = it
                    },
                    weatherLocation = weatherLocation,
                )
                ConditionType.Visibility -> Unit
                ConditionType.Pressure -> Unit
            }
            val interactionSource by remember { mutableStateOf(MutableInteractionSource()) }
            if (!popupExpanded) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(horizontal = 16.dp)
                ) {
                    Button(
                        shapes = ButtonDefaults.shapes(),
                        onClick = {
                            expandedMenu = !expandedMenu
                        },
                        interactionSource = interactionSource,
                        modifier = Modifier.size(
                            width = 80.dp,
                            height = 36.dp,
                        ),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painterResource(viewState.type.getIcon()),
                                when (viewState.type) {
                                    ConditionType.Conditions -> stringResource(StringR.string.conditions)
                                    ConditionType.UvIndex -> stringResource(StringR.string.uv_index)
                                    ConditionType.Wind -> stringResource(StringR.string.wind)
                                    ConditionType.Precipitation -> stringResource(StringR.string.precipitation)
                                    ConditionType.Humidity -> stringResource(StringR.string.humidity)
                                    ConditionType.Visibility -> stringResource(StringR.string.visibility)
                                    ConditionType.Pressure -> stringResource(StringR.string.pressure)
                                },
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                painterResource(R.drawable.ic_arrow_down),
                                contentDescription = null,
                                modifier = Modifier.rotate(menuIconRotation),
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = expandedMenu,
                        shape = WeatherYouTheme.shapes.large,
                        onDismissRequest = {
                            expandedMenu = false
                        },
                    ) {
                        listOf(
                            ConditionType.Conditions,
                            ConditionType.UvIndex,
                            ConditionType.Wind,
                            ConditionType.Precipitation
                        ).forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = it.name,
                                        style = WeatherYouTheme.typography.bodyMedium,
                                        color = WeatherYouTheme.colorScheme.onSurface,
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        painterResource(it.getIcon()),
                                        when (it) {
                                            ConditionType.Conditions -> stringResource(StringR.string.conditions)
                                            ConditionType.UvIndex -> stringResource(StringR.string.uv_index)
                                            ConditionType.Wind -> stringResource(StringR.string.wind)
                                            ConditionType.Precipitation -> stringResource(StringR.string.precipitation)
                                            ConditionType.Humidity -> stringResource(StringR.string.humidity)
                                            ConditionType.Visibility -> stringResource(StringR.string.visibility)
                                            ConditionType.Pressure -> stringResource(StringR.string.pressure)
                                        },
                                        modifier = Modifier.size(24.dp),
                                    )
                                },
                                onClick = {
                                    onTypeChange(it)
                                    expandedMenu = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(200.dp))
    }
}

@Composable
fun ConditionType.getIcon(): Int = when (this) {
    ConditionType.Conditions -> R.drawable.ic_thermostat
    ConditionType.UvIndex -> com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_sunny
    ConditionType.Wind -> com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_air
    ConditionType.Precipitation -> com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_weather_rainyday
    ConditionType.Humidity -> com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_water_drop
    ConditionType.Visibility -> com.rodrigmatrix.weatheryou.locationdetails.R.drawable.ic_visibility
    ConditionType.Pressure -> com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_pressure
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
