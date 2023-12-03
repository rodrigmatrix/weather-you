package com.rodrigmatrix.weatheryou.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateWidgetTemperatureUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateWidgetWeatherDataWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params), KoinComponent {

    private val updateWidgetTemperatureUseCase by inject<UpdateWidgetTemperatureUseCase>()

    override suspend fun doWork(): Result {
        val widgetWeather = updateWidgetTemperatureUseCase()
            .flowOn(Dispatchers.IO)
            .firstOrNull()

        // TODO force widgets update

        return if (widgetWeather != null) {
            Result.success()
        } else {
            Result.failure()
        }
    }
}