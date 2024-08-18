package com.rodrigmatrix.weatheryou.tv.presentation.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.Icon
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import com.rodrigmatrix.weatheryou.components.theme.ColorMode
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import com.rodrigmatrix.weatheryou.tv.R
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppColorPreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppThemePreferenceUseCase
import com.rodrigmatrix.weatheryou.tv.presentation.theme.WeatherYouTvTheme
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val getAppColorPreferenceUseCase: GetAppColorPreferenceUseCase by inject()
    private val getAppThemePreferenceUseCase: GetAppThemePreferenceUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var colorMode by remember { mutableStateOf(ColorMode.Dynamic) }
            var themeMode by remember { mutableStateOf(ThemeMode.Dark) }
            LaunchedEffect(Unit) {
                getAppColorPreferenceUseCase().collect {
                    colorMode = when (it) {
                        AppColorPreference.DYNAMIC -> ColorMode.Dynamic
                        AppColorPreference.DEFAULT -> ColorMode.Default
                        AppColorPreference.MOSQUE -> ColorMode.Mosque
                        AppColorPreference.DARK_FERN -> ColorMode.DarkFern
                        AppColorPreference.FRESH_EGGPLANT -> ColorMode.FreshEggplant
                    }
                }
                getAppThemePreferenceUseCase().collect {
                    themeMode = when (it) {
                        AppThemePreference.SYSTEM_DEFAULT -> ThemeMode.System
                        AppThemePreference.LIGHT -> ThemeMode.Light
                        AppThemePreference.DARK -> ThemeMode.Dark
                    }
                }
            }
            WeatherYouTvTheme(
                themeMode = themeMode,
                colorMode = colorMode,
            ) {
                val navController = rememberNavController()
                NavigationDrawer(
                    drawerContent = {
                        Column {
                            Spacer(Modifier.height(8.dp))
                            NavigationDrawerItem(
                                selected = false,
                                onClick = { },
                                leadingContent = {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_launcher),
                                        contentDescription = null,
                                        modifier = Modifier.clip(CircleShape),
                                    )
                                },
                                modifier = Modifier
                                    .focusable(enabled = false)
                                    .focusProperties {
                                        canFocus = false
                                    }
                                    .padding(horizontal = 8.dp),
                            ) {
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    style = WeatherYouTheme.typography.headlineSmall,
                                    color = WeatherYouTheme.colorScheme.onSurface,
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(
                                    space = 8.dp,
                                    alignment = Alignment.CenterVertically,
                                ),
                                modifier = Modifier.fillMaxHeight(),
                            ) {
                                TvScreenEntry.entries.forEach { entry ->
                                    NavigationDrawerItem(
                                        selected = navController.currentDestination?.route == entry.route.toString(),
                                        onClick = {
                                            navController.navigate(entry.route)
                                        },
                                        leadingContent = {
                                            Icon(
                                                imageVector = entry.icon,
                                                tint = WeatherYouTheme.colorScheme.onSurface,
                                                contentDescription = null,
                                            )
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                    ) {
                                        Text(
                                            text = stringResource(entry.stringRes),
                                            style = WeatherYouTheme.typography.titleMedium,
                                            color = WeatherYouTheme.colorScheme.onSurface,
                                        )
                                    }
                                }
                            }
                        }
                    },
                ) {
                    WeatherYouTvNavHost(navController)
                }
            }
        }
    }
}