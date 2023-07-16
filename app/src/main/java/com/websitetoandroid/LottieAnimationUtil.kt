package com.websitetoandroid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun ShowLottieAnimation(rawId:Int) {


    val isPlaying by remember {
        mutableStateOf(true)
    }
    val speed by remember {
        mutableFloatStateOf(1f)
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(rawId)
    )


    val progress by animateLottieCompositionAsState(
        composition,

        iterations = LottieConstants.IterateForever,

        isPlaying = isPlaying,

        speed = speed,

        restartOnPlay = false

    )

    LottieAnimation(
        composition = composition,
        progress = progress
    )


}