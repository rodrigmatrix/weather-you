package com.rodrigmatrix.weatheryou.ads.manager

import android.app.Activity

interface AdsManager {

    fun showRewardedInterstitial(
        activity: Activity,
        showAd: Boolean,
        flagId: String,
        onRewardGranted: () -> Unit,
    )
}