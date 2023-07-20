package com.websitetoandroid.data

import androidx.annotation.Keep
import androidx.compose.ui.graphics.vector.ImageVector

@Keep
data class BottomBarItem(
    val title : String,
    val icon : ImageVector,
    val url : String
)
