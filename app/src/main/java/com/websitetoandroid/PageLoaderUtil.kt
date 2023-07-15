package com.websitetoandroid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ShowLoader(modifier: Modifier,loaderType:String){

    when(loaderType){
        "CIRCULAR" -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        "ANIMATION" -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
               ShowLottieAnimation()
            }
        }
        "NONE" -> {
            return
        }
        else -> {
            return
        }
    }
}