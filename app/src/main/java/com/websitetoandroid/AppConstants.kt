package com.websitetoandroid

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import com.websitetoandroid.data.BottomBarItem

object AppConstants {

    const val EntryUrl : String = "https://w3schools.com"
    const val EnablePullRefresh : Boolean = true
    const val StatusBarColor : String = "#FFFFFF"
    const val ShowSplashTime : Int = 5
    const val PageLoaderType : String = "ANIMATION"
    const val BannerAdId : String = "ca-app-pub-3940256099942544/6300978111"
    val BottomAppBar : List<BottomBarItem> = listOf(
        BottomBarItem("title",Icons.Default.Add,"https://hi.com"),
        BottomBarItem("title",Icons.Default.List,"https://hi.com"),
        BottomBarItem("title",Icons.Default.Build,"https://hi.com"),
        BottomBarItem("title",Icons.Default.AccountBox,"https://hi.com"),
    )
    val OpenExternally : List<String> = listOf(
        "facebook",
        "youtube",
        "instagram"
    )

}