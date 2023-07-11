package com.websitetoandroid

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable

@Composable
fun ShowCircularLoader(){
    if (AppConstants.ENABLE_CIRCULAR_LOADER){
        CircularProgressIndicator()
    }
}