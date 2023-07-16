package com.websitetoandroid.feature_ads

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class AdsHandler(
    private val activity: Activity
) {

    @SuppressLint("MissingPermission")
    fun getBannerAdView(bannerAdId:String) : View?{

        if (bannerAdId.isBlank()) return null

        val adRequest = AdRequest.Builder().build()

        return AdView(activity).apply {
            adUnitId = bannerAdId
            setAdSize(AdSize.FULL_BANNER)
            loadAd(adRequest)
        }

    }




}