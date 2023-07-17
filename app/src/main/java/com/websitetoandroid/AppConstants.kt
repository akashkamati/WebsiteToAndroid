package com.websitetoandroid

import com.websitetoandroid.data.BottomBarItem

object AppConstants {

    const val EntryUrl : String = "https://w3schools.com"
    const val EnablePullRefresh : Boolean = true
    const val StatusBarColor : String = "#FFFFFF"
    const val ShowSplashTime : Int = 5
    const val PageLoaderType : String = "ANIMATION"
    const val BannerAdId : String = "ca-app-pub-3940256099942544/6300978111"
    val BottomAppBar : List<BottomBarItem> = listOf(
        BottomBarItem("title","fas fa-address-book","https://hi.com"),
        BottomBarItem("title","fas fa-address-book","https://hi.com"),
        BottomBarItem("title","fas fa-address-book","https://hi.com"),
        BottomBarItem("title","fas fa-address-book","https://hi.com"),
    )

}