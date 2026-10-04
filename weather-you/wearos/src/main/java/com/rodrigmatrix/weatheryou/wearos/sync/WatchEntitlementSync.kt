package com.rodrigmatrix.weatheryou.wearos.sync

import android.content.Context
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.rodrigmatrix.weatheryou.domain.model.DonationEntitlementContract
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

private object WatchSettings : KoinComponent {
    val repository: SettingsRepository by inject()
}

enum class EntitlementRefreshResult {
    ENTITLED,
    NOT_ENTITLED,
    PHONE_NOT_SYNCED,
    UNAVAILABLE,
}

fun refreshEntitlementFromPhone(
    context: Context,
    onComplete: (EntitlementRefreshResult) -> Unit = {},
) {
    Wearable.getDataClient(context).getDataItems()
        .addOnSuccessListener { items ->
            try {
                val dataItem = items.firstOrNull { it.uri.path == DonationEntitlementContract.DATA_PATH }
                if (dataItem == null) {
                    onComplete(EntitlementRefreshResult.PHONE_NOT_SYNCED)
                } else {
                    val entitled = DataMapItem.fromDataItem(dataItem).dataMap
                        .getBoolean(DonationEntitlementContract.DATA_KEY, false)
                    serviceScope.launch {
                        try {
                            WatchSettings.repository.setIsPremiumUser(entitled).first()
                            withContext(Dispatchers.Main.immediate) {
                                onComplete(
                                    if (entitled) EntitlementRefreshResult.ENTITLED
                                    else EntitlementRefreshResult.NOT_ENTITLED,
                                )
                            }
                        } catch (_: Exception) {
                            withContext(Dispatchers.Main.immediate) {
                                onComplete(EntitlementRefreshResult.UNAVAILABLE)
                            }
                        }
                    }
                }
            } finally {
                items.release()
            }
        }
        .addOnFailureListener { onComplete(EntitlementRefreshResult.UNAVAILABLE) }
}

class WatchEntitlementListenerService : WearableListenerService() {
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        try {
            dataEvents.forEach { event ->
                if (event.type != DataEvent.TYPE_CHANGED ||
                    event.dataItem.uri.path != DonationEntitlementContract.DATA_PATH
                ) return@forEach

                val entitled = DataMapItem.fromDataItem(event.dataItem).dataMap
                    .getBoolean(DonationEntitlementContract.DATA_KEY, false)
                serviceScope.launch {
                    WatchSettings.repository.setIsPremiumUser(entitled).first()
                }
            }
        } finally {
            dataEvents.release()
        }
    }
}
