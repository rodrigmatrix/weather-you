package com.rodrigmatrix.weatheryou.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateWidgetTemperatureUseCase
import com.rodrigmatrix.weatheryou.widgets.weather.CurrentWeatherWidget
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateWidgetWeatherDataWorker(
    private val appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params), KoinComponent {

    private val updateWidgetTemperatureUseCase by inject<UpdateWidgetTemperatureUseCase>()

    override suspend fun doWork(): Result {
        val widgetWeather = updateWidgetTemperatureUseCase()
            .flowOn(Dispatchers.IO)
            .firstOrNull()

        return if (widgetWeather != null) {
            CurrentWeatherWidget().updateWidget(appContext)
            Result.success()
        } else {
            Result.failure()
        }
    }
}