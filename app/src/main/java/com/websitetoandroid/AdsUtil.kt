package com.websitetoandroid

import android.view.View
import com.websitetoandroid.feature_ads.AdsHandler

class AdsUtil(
    activity: MainActivity
) {

    private val adHandler = AdsHandler(activity)

    fun getBannerId(bannerAdId:String) : View?{
        return adHandler.getBannerAdView(bannerAdId)
    }

}