package com.websitetoandroid

import android.app.Activity
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties

@Composable
fun MainScreen(activity: Activity, webView: WebView) {

    val showExitDialog = remember {
        mutableStateOf(false)
    }

    BackHandler {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            showExitDialog.value = true
        }
    }

    if (showExitDialog.value) {
        AlertDialog(
            title = { Text(text = "Do you want to exit from app?") },
            onDismissRequest = {
                showExitDialog.value = false
            },
            confirmButton = {
                Text(text = "Yes", fontSize = 20.sp, modifier = Modifier.clickable { activity.finishAndRemoveTask() })
            },
            dismissButton = {
                Text(text = "No",fontSize = 20.sp,  modifier = Modifier.clickable { showExitDialog.value = false })
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,


            )
        )
    }

    AndroidView(
        factory = {
            webView.apply {
                loadUrl(AppConstants.ENTRY_URL)
                webViewClient = CustomWebViewClient(activity)
                webChromeClient = CustomWebChromeClient()
            }

        },
        modifier = Modifier.fillMaxSize()
    )
}