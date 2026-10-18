package com.rodrigmatrix.weatheryou.widgets.weather

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateWidgetLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.EnsureWidgetLocationUseCase
import com.rodrigmatrix.weatheryou.widgets.weather.animated.CurrentAnimatedWeatherWidget
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException
import java.util.concurrent.TimeUnit

internal const val MAX_SYNC_ATTEMPTS = 3
internal const val WIDGET_WEATHER_REFRESH_TIMEOUT_MILLIS = 60_000L

internal fun shouldRetryWidgetWeatherSync(runAttemptCount: Int): Boolean =
    runAttemptCount + 1 < MAX_SYNC_ATTEMPTS

fun enqueueWidgetWeatherRefresh(
    context: Context,
    existingWorkPolicy: ExistingWorkPolicy = ExistingWorkPolicy.KEEP,
) {
    val request = OneTimeWorkRequestBuilder<WidgetWeatherSyncWorker>()
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        WIDGET_WEATHER_SYNC_WORK,
        existingWorkPolicy,
        request,
    )
}

internal suspend fun runWidgetWeatherRefreshWithTimeout(
    timeoutMillis: Long = WIDGET_WEATHER_REFRESH_TIMEOUT_MILLIS,
    refresh: suspend () -> Unit,
) {
    val completed = withTimeoutOrNull(timeoutMillis) {
        refresh()
        true
    } ?: false

    if (!completed) {
        throw IOException("Widget weather refresh timed out after $timeoutMillis ms")
    }
}

class WidgetWeatherSyncWorker(
    appContext: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    private val updateWidgetLocationsUseCase by inject<UpdateWidgetLocationsUseCase>()
    private val ensureWidgetLocationUseCase by inject<EnsureWidgetLocationUseCase>()

    override suspend fun doWork(): Result {
        return try {
            runWidgetWeatherRefreshWithTimeout {
                updateWidgetRefreshState(applicationContext, failed = false)
                ensureLocationsForActiveWidgets()
                updateWidgetLocationsUseCase().first()
                updateWidgetRefreshState(applicationContext, failed = false)
            }
            Result.success()
        } catch (cancelled: CancellationException) {
            kotlinx.coroutines.withContext(kotlinx.coroutines.NonCancellable) {
                updateWidgetRefreshState(applicationContext, failed = true)
            }
            throw cancelled
        } catch (failure: Exception) {
            updateWidgetRefreshState(applicationContext, failed = true)
            if (shouldRetryWidgetWeatherSync(runAttemptCount)) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    private suspend fun ensureLocationsForActiveWidgets() {
        val manager = GlanceAppWidgetManager(applicationContext)
        val widgetIds = manager.getGlanceIds(CurrentWeatherWidget::class.java) +
            manager.getGlanceIds(CurrentAnimatedWeatherWidget::class.java)

        widgetIds.forEach { glanceId ->
            val appWidgetId = manager.getAppWidgetId(glanceId).toString()
            ensureWidgetLocationUseCase(appWidgetId).first()
        }
    }
}

private suspend fun updateWidgetRefreshState(context: Context, failed: Boolean) {
    val widgets = listOf(CurrentWeatherWidget(), CurrentAnimatedWeatherWidget())
    val manager = GlanceAppWidgetManager(context)

    widgets.forEach { widget ->
        manager.getGlanceIds(widget::class.java).forEach { glanceId ->
            updateAppWidgetState(context, glanceId) { preferences ->
                preferences[widgetRefreshFailedKey] = failed
            }
        }
        widget.updateAll(context)
    }
}
