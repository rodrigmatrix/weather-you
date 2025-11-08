package com.rodrigmatrix.weatheryou.ads.manager

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.rodrigmatrix.weatheryou.domain.repository.RemoteConfigRepository
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull

class AdsManagerImpl(
    private val remoteConfigRepository: RemoteConfigRepository,
    private val settingsRepository: SettingsRepository,
) : AdsManager {

    override suspend fun showRewardedInterstitial(
        activity: Activity,
        showAd: Boolean,
        flagId: String,
        onRewardGranted: () -> Unit,
    ) {
        showAd(showAd, onRewardGranted) {
            val adRequest = AdRequest.Builder().build()
            val adId = remoteConfigRepository.getString(flagId)

            RewardedInterstitialAd.load(activity, adId, adRequest, object : RewardedInterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    onRewardGranted()
                }

                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    ad.show(activity) { onRewardGranted() }
                }
            })
        }
    }

    private suspend fun showAd(
        showAd: Boolean,
        rewardGrantedCallback: () -> Unit,
        adContent : () -> Unit,
    ) {
        if (showAd && remoteConfigRepository.getBoolean("show_ads") &&
            settingsRepository.getIsPremiumUser().firstOrNull() == false) {
            adContent()
        } else {
            rewardGrantedCallback()
        }
    }
}