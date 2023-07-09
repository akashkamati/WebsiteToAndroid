package com.websitetoandroid

import android.app.Activity
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import com.websitetoandroid.pull_to_refresh.PullRefreshIndicator
import com.websitetoandroid.pull_to_refresh.pullRefresh
import com.websitetoandroid.pull_to_refresh.rememberPullRefreshState

@Composable
fun MainScreen(activity: Activity, webView: WebView) {
    val showExitDialog = remember {
        mutableStateOf(false)
    }

    val isRefreshing = remember {
        mutableStateOf(false)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing.value,
        onRefresh = {
            webView.reload()
        }
    )

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
                Text(
                    text = "Yes",
                    fontSize = 20.sp,
                    modifier = Modifier.clickable { activity.finishAndRemoveTask() })
            },
            dismissButton = {
                Text(
                    text = "No",
                    fontSize = 20.sp,
                    modifier = Modifier.clickable { showExitDialog.value = false })
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            )
        )
    }

    Box(
        Modifier
            .pullRefresh(pullRefreshState, AppConstants.ENABLE_PULL_REFRESH)
            .verticalScroll(rememberScrollState())
    ) {

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
        PullRefreshIndicator(
            refreshing = isRefreshing.value,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Color.White,
            backgroundColor = Color.Black
        )
    }
}