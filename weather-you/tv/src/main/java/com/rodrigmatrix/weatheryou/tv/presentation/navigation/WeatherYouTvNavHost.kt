package com.rodrigmatrix.weatheryou.tv.presentation.navigation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeViewModel
import com.rodrigmatrix.weatheryou.presentation.about.AboutScreen
import com.rodrigmatrix.weatheryou.tv.presentation.home.TvWeatherLocationsScreen
import com.rodrigmatrix.weatheryou.tv.presentation.locations.TVWeatherLocationsViewModel
import com.rodrigmatrix.weatheryou.tv.presentation.search.SearchLocationScreen
import com.rodrigmatrix.weatheryou.tv.presentation.settings.TvSettingsScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WeatherYouTvNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = koinViewModel(),
) {
    val infiniteTransition = rememberInfiniteTransition(label = "particleTick")
    val particleTick by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1_000_000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 100_000,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "particleTick",
    )
    NavHost(
        navController,
        startDestination = TvRoutes.Home,
        modifier = modifier.background(WeatherYouTheme.colorScheme.surface),
    ) {
        composable<TvRoutes.Home> {
            TvWeatherLocationsScreen(
                viewModel = homeViewModel,
                particleTick = particleTick.toLong(),
                navController = navController,
            )
        }

        composable<TvRoutes.Search> {
            SearchLocationScreen(navController = navController)
        }

        composable<TvRoutes.Settings> {
            TvSettingsScreen()
        }

        composable<TvRoutes.About> {
            AboutScreen()
        }
    }
}