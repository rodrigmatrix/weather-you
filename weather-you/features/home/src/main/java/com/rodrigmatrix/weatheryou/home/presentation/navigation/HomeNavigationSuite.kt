package com.rodrigmatrix.weatheryou.home.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeNavigationSuite(
    navController: NavController,
    currentDestination: String,
    onSearchClick: () -> Unit,
    homeScreenNavigator: ThreePaneScaffoldNavigator<Int>,
    modifier: Modifier = Modifier,
) {
    if (
        homeScreenNavigator.currentDestination?.contentKey == null &&
        currentDestination != "add_location"
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, start = 16.dp, end = 16.dp)
                .padding(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HorizontalFloatingToolbar(
                expanded = true,
                modifier = Modifier.weight(1f),
            ) {
                HomeEntry.entries.forEach { screen ->
                    val isSelected = currentDestination == screen.route
                    NavigationItem(
                        isSelected = isSelected,
                        screen = screen,
                        onClick = {
                            if (!isSelected) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            AnimatedVisibility(currentDestination == HomeEntry.Locations.route) {
                val fabInteractionSource = remember { MutableInteractionSource() }
                val isFabPressed by fabInteractionSource.collectIsPressedAsState()
                val fabCornerRadius by animateDpAsState(
                    targetValue = if (isFabPressed) 20.dp else 28.dp,
                    animationSpec = spring(
                        dampingRatio = 0.6f,
                        stiffness = 500f
                    ),
                    label = "fabCornerRadius"
                )

                MediumFloatingActionButton(
                    onClick = onSearchClick,
                    containerColor = WeatherYouTheme.colorScheme.secondaryContainer,
                    contentColor = WeatherYouTheme.colorScheme.onSecondaryContainer,
                    shape = RoundedCornerShape(fabCornerRadius),
                    interactionSource = fabInteractionSource,
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_location),
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.NavigationItem(
    isSelected: Boolean,
    screen: HomeEntry,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val indicatorColor by animateColorAsState(
        targetValue = when {
            isPressed -> WeatherYouTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            isSelected -> WeatherYouTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            else -> Color.Transparent
        },
        animationSpec = spring(stiffness = 500f),
        label = "indicatorColor"
    )

    val indicatorCorner by animateDpAsState(
        targetValue = if (!isSelected) 12.dp else 16.dp,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "indicatorCorner"
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .height(64.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(
                    color = indicatorColor,
                    shape = RoundedCornerShape(indicatorCorner)
                )
        )

        Icon(
            painter = painterResource(screen.icon),
            contentDescription = screen.route,
            tint = WeatherYouTheme.colorScheme.onSurfaceVariant,
        )
    }
}