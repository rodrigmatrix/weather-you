package com.rodrigmatrix.weatheryou.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherYouSmallAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = WeatherYouTheme.colorScheme.background,
    ),
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    Box(modifier = modifier) {
        TopAppBar(
            modifier = modifier,
            actions = actions,
            title = title,
            navigationIcon = navigationIcon,
            colors = colors,
            scrollBehavior = scrollBehavior,
        )
    }
}