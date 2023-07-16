package com.websitetoandroid

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }

}

@Composable
fun NoInternetScreen(modifier: Modifier,onRetry : () -> Unit,closeApp : ()-> Unit) {

    val showExitDialog = remember {
        mutableStateOf(false)
    }

    if (showExitDialog.value) {
        ExitFromAppDialog(
            onConfirm = {
                showExitDialog.value = false
                closeApp()
            },
            onDismiss = {showExitDialog.value = false  }
        )
    }



    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = modifier.weight(1f)) {
            ShowLottieAnimation(rawId = R.raw.no_internet_animation)
        }

        Button(onClick = { onRetry() }) {
          Text(text = "Retry")
        }

    }
    BackHandler {
        showExitDialog.value = true
    }

}