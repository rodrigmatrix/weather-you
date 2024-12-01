package com.rodrigmatrix.weatheryou.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateLocationsUseCase
import com.rodrigmatrix.weatheryou.widgets.weather.CurrentWeatherWidget
import com.rodrigmatrix.weatheryou.widgets.weather.animated.CurrentAnimatedWeatherWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateWidgetWeatherDataWorker(
    private val appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params), KoinComponent {

    private val updateLocationsUseCase by inject<UpdateLocationsUseCase>()

    override suspend fun doWork(): Result {
        updateLocationsUseCase()
            .flowOn(Dispatchers.IO)
            .firstOrNull()
        CurrentWeatherWidget().updateAll(appContext)
        CurrentAnimatedWeatherWidget().updateAll(appContext)
        return Result.success()
    }
}