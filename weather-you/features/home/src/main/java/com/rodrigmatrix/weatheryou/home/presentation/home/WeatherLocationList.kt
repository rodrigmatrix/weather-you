package com.rodrigmatrix.weatheryou.home.presentation.home

import android.content.res.Configuration
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.WeatherLocationCardContent
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.particle.WeatherAnimationsBackground
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherList
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.components.theme.md_theme_dark_primaryContainer
import com.rodrigmatrix.weatheryou.components.theme.md_theme_dark_secondaryContainer
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import sh.calvin.reorderable.ReorderableItem
import androidx.compose.runtime.setValue
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import sh.calvin.reorderable.rememberReorderableLazyGridState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherLocationList(
    weatherLocationList: List<WeatherLocation>,
    isRefreshingLocations: Boolean,
    selectedLocation: WeatherLocation?,
    onItemClick: (WeatherLocation) -> Unit,
    onDismiss: (WeatherLocation) -> Unit,
    onOrderChanged: (List<WeatherLocation>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val view = LocalView.current
    var list by remember {
        mutableStateOf(weatherLocationList)
    }.apply { value = weatherLocationList }

    val listState = rememberLazyGridState()
    val reorderableLazyListState = rememberReorderableLazyGridState(listState) { from, to ->
        list = list.toMutableList().apply { add(to.index, removeAt(from.index)) }
        onOrderChanged(list)
        view.performHapticAction(HapticAction.VirtualKey)
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        state = listState,
        modifier = modifier.fillMaxHeight(),
    ) {
        items(
            items = list,
            key = { it.id },
        ) { item ->
            if (item.isCurrentLocation) {
                WeatherLocation(
                    weatherLocation = item,
                    isRefreshingLocations = isRefreshingLocations,
                    isSelected = selectedLocation == item,
                    onItemClick = onItemClick,
                    onDismiss = onDismiss,
                )
            } else {
                ReorderableItem(
                    state = reorderableLazyListState,
                    key = item.id,
                ) { isDragging ->
                    val scale by animateFloatAsState(if (isDragging) 1.1f else 1f, label = "")
                    WeatherLocation(
                        weatherLocation = item,
                        isRefreshingLocations = isRefreshingLocations,
                        isSelected = selectedLocation == item,
                        onItemClick = onItemClick,
                        onDismiss = onDismiss,
                        modifier = Modifier.longPressDraggableHandle(
                            onDragStarted = {
                                view.performHapticAction(HapticAction.DragStart)
                            },
                            onDragStopped = {
                                view.performHapticAction(HapticAction.DragEnd)
                            },
                        ).scale(scale),
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherLocation(
    weatherLocation: WeatherLocation,
    isSelected: Boolean,
    isRefreshingLocations: Boolean,
    onItemClick: (WeatherLocation) -> Unit,
    onDismiss: (WeatherLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    WeatherYouTheme(
        themeMode = if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
            ThemeMode.Dark
        } else {
            WeatherYouTheme.themeMode
        },
        colorMode = WeatherYouTheme.colorMode,
        themeSettings = WeatherYouTheme.themeSettings,
    ) {
        WeatherYouCard(
            color = if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
                if (WeatherYouTheme.themeSettings.enableThemeColorForWeatherAnimations) {
                    if (isSelected) {
                        WeatherYouTheme.colorScheme.primaryContainer
                    } else {
                        WeatherYouTheme.colorScheme.secondaryContainer
                    }.copy(alpha = 0.4f)
                } else {
                    if (isSelected) {
                        md_theme_dark_primaryContainer
                    } else {
                        md_theme_dark_secondaryContainer
                    }.copy(alpha = 0.4f)
                }
            } else {
                WeatherYouTheme.colorScheme.secondaryContainer
            },
            isDismissible = false,
            onClick = {
                onItemClick(weatherLocation)
            },
            onDismiss = {
                onDismiss(weatherLocation)
            },
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Box{
                if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
                    WeatherAnimationsBackground(
                        weatherLocation = weatherLocation,
                        modifier = Modifier.height(130.dp),
                    )
                }
                WeatherLocationCardContent(
                    weatherLocation = weatherLocation,
                    isRefreshingLocations = isRefreshingLocations,
                )
            }
        }
    }
}

private fun View.performHapticAction(action: HapticAction) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        performHapticFeedback(
            when (action) {
                HapticAction.VirtualKey -> HapticFeedbackConstants.VIRTUAL_KEY
                HapticAction.DragStart -> HapticFeedbackConstants.DRAG_START
                HapticAction.DragEnd -> HapticFeedbackConstants.GESTURE_END
            }
        )
    } else {
        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
}

enum class HapticAction {
    VirtualKey,
    DragStart,
    DragEnd,
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeatherLocationPreview() {
    WeatherYouTheme {
        WeatherLocationList(
            weatherLocationList = PreviewWeatherList,
            isRefreshingLocations = false,
            selectedLocation = null,
            onItemClick = { },
            onDismiss = { },
            onOrderChanged = { },
        )
    }
}