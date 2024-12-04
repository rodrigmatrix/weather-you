package com.rodrigmatrix.weatheryou.ads.manager

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.rodrigmatrix.weatheryou.domain.repository.RemoteConfigRepository

class AdsManagerImpl(
    private val remoteConfigRepository: RemoteConfigRepository,
) : AdsManager {

    override fun showRewardedInterstitial(
        activity: Activity,
        showAd: Boolean,
        flagId: String,
        onRewardGranted: () -> Unit,
    ) {
        showAd(showAd, onRewardGranted) {
            val adRequest = AdRequest.Builder().build()
            val adId = remoteConfigRepository.getString(flagId)

            RewardedAd.load(activity, adId, adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    onRewardGranted()
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    ad.show(activity) { onRewardGranted() }
                }
            })
        }
    }

    private fun showAd(
        showAd: Boolean,
        rewardGrantedCallback: () -> Unit,
        adContent : () -> Unit,
    ) {
        if (showAd && remoteConfigRepository.getBoolean("show_ads")) {
            adContent()
        } else {
            rewardGrantedCallback()
        }
    }
}