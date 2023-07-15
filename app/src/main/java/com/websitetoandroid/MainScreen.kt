package com.websitetoandroid

import android.app.Activity
import android.webkit.WebSettings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState
import com.websitetoandroid.pull_to_refresh.PullRefreshIndicator
import com.websitetoandroid.pull_to_refresh.pullRefresh
import com.websitetoandroid.pull_to_refresh.rememberPullRefreshState

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    activity: Activity,
    state: WebViewState,
    customWebChromeClient: CustomWebChromeClient,
    navigator: WebViewNavigator,
) {

    val isLoadingForFirstTime = remember {
        mutableStateOf(true)
    }


    val showExitDialog = remember {
        mutableStateOf(false)
    }

    val isRefreshing = remember {
        mutableStateOf(false)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing.value,
        onRefresh = {
            navigator.reload()
        }
    )

    BackHandler {
        if (navigator.canGoBack) {
            navigator.navigateBack()
        } else {
            showExitDialog.value = true
        }
    }

    SideEffect {
        if (isLoadingForFirstTime.value){
            if (!state.isLoading){
                isLoadingForFirstTime.value = false
            }
        }
    }


        Box(
            modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState, AppConstants.ENABLE_PULL_REFRESH)
                .verticalScroll(rememberScrollState()),
        ) {
            WebView(
                state = state,
                captureBackPresses = true,
                onCreated = {
                    it.settings.apply {
                        cacheMode = WebSettings.LOAD_DEFAULT
                        userAgentString =
                            "Mozilla/5.0 (Linux; Android 9; ONEPLUS A3003) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.92 Mobile Safari/537.36"
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        allowContentAccess = true
                        allowFileAccess = true
                        loadsImagesAutomatically = true
                        mediaPlaybackRequiresUserGesture = false
                    }
                },
                chromeClient = customWebChromeClient,
                client = CustomWebViewClient(activity),
                modifier = modifier.fillMaxSize(),
                navigator = navigator
            )
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

    if (state.isLoading && !isLoadingForFirstTime.value) {
        ShowLoader(modifier = modifier)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        PullRefreshIndicator(
            refreshing = isRefreshing.value,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Color.White,
            backgroundColor = Color.Black
        )
    }



}