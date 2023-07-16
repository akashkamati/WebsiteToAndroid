package com.websitetoandroid

import android.annotation.SuppressLint
import android.app.Activity
import android.webkit.WebSettings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState
import com.websitetoandroid.pull_to_refresh.PullRefreshIndicator
import com.websitetoandroid.pull_to_refresh.pullRefresh
import com.websitetoandroid.pull_to_refresh.rememberPullRefreshState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    activity: Activity,
    state: WebViewState,
    customWebChromeClient: CustomWebChromeClient,
    navigator: WebViewNavigator,
    enablePullRefresh: Boolean,
    loaderType: String,
    noInternetCB: () -> Unit,
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
        if (isLoadingForFirstTime.value && !state.isLoading && state.lastLoadedUrl != null) {
            isLoadingForFirstTime.value = false
        }
    }


    Box(
        modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState, enablePullRefresh)
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
            client = CustomWebViewClient(activity, noInternetCB = {
                if (!isInternetAvailable(activity)) {
                    noInternetCB()
                }
            }),
            modifier = modifier.fillMaxSize(),
            navigator = navigator
        )

    }


    if (showExitDialog.value) {
        ExitFromAppDialog(
            onConfirm = {
                showExitDialog.value = false
                activity.finishAndRemoveTask()
            },
            onDismiss = { showExitDialog.value = false }
        )
    }

    if (state.isLoading && !isLoadingForFirstTime.value && state.lastLoadedUrl != null) {
        ShowLoader(modifier = modifier, loaderType = loaderType)
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