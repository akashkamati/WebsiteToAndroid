package com.websitetoandroid

import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.websitetoandroid.ui.theme.WebsiteToAndroidTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            WebsiteToAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    val state = rememberWebViewState(url = AppConstants.ENTRY_URL)
                    val navigator = rememberWebViewNavigator()
                    val navController = rememberNavController()



                    val fileChooserLauncher = rememberLauncherForActivityResult(
                        contract = FileChooserContract(),
                        onResult = {
                            if (it != null) {
                                filePathCallback?.onReceiveValue(it.toTypedArray())
                            }
                        })

                    val customWebChromeClient =
                        CustomWebChromeClient { callback, params ->
                            filePathCallback = callback
                            fileChooserLauncher.launch(params)
                        }


                    NavHost(navController = navController, startDestination = "splash"){

                        composable("splash"){
                            LaunchedEffect(key1 = true) {
                                delay((AppConstants.SHOW_SPLASH_TIME *1000).toLong())
                                navController.navigate("main")
                            }
                            ShowSplashScreen()
                        }

                        composable("main"){
                            MainScreen(
                                activity = this@MainActivity,
                                state = state,
                                customWebChromeClient = customWebChromeClient,
                                navigator = navigator,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
