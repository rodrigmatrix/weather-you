package com.rodrigmatrix.weatheryou.settings.utils

interface AppThemeManager {

    suspend fun setAppTheme(enableFollowSystem: Boolean = true)
}