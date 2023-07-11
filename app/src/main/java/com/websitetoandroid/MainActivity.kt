package com.websitetoandroid

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.websitetoandroid.ui.theme.WebsiteToAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            WebsiteToAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val webView = WebView(this).apply {
                        settings.cacheMode = WebSettings.LOAD_DEFAULT
                        settings.userAgentString = "Mozilla/5.0 (Linux; Android 9; ONEPLUS A3003) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.92 Mobile Safari/537.36"
                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.allowContentAccess = true
                        settings.allowFileAccess = true
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                    }
                    WebView.setWebContentsDebuggingEnabled(true)
                    MainScreen(this,webView)
                }
            }
        }
    }
}
