package com.websitetoandroid.data

import androidx.annotation.Keep

@Keep
data class BottomBarItem(
    val title : String,
    val icon : String,
    val url : String
)
