package com.websitetoandroid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShowLoader(modifier: Modifier,loaderType:String){

    when(loaderType){
        "CIRCULAR" -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        "ANIMATION" -> {
            Box(modifier = modifier.size(400.dp), contentAlignment = Alignment.Center) {
               ShowLottieAnimation(R.raw.page_loader)
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