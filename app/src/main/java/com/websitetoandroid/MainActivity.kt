package com.websitetoandroid

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.web.WebViewState
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.websitetoandroid.data.AppConstants
import com.websitetoandroid.data.DataUtil
import com.websitetoandroid.ui.theme.WebsiteToAndroidTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private var state: WebViewState? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            WebsiteToAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorScheme.background
                ) {

                    val appConstants: MutableState<AppConstants?> = remember {
                        mutableStateOf(null)
                    }
                    val navigator = rememberWebViewNavigator()

                    if (appConstants.value != null) {
                        this.window.apply {
                            if (appConstants.value!!.StatusBarColor.isNotBlank()) {
                                statusBarColor =
                                    Color.parseColor(appConstants.value!!.StatusBarColor)
                            }
                        }
                    }


                    LaunchedEffect(key1 = true) {
                        val constants = DataUtil.getAppConstants(this@MainActivity)
                        if (constants == null) {
                            finishAndRemoveTask()
                        } else {
                            appConstants.value = constants
                        }
                    }

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


                    NavHost(navController = navController, startDestination = "splash") {

                        composable("splash") {
                            LaunchedEffect(key1 = true) {
                                if (!isInternetAvailable(this@MainActivity)) {
                                    navController.navigate("no_internet")
                                } else if (isInternetAvailable(this@MainActivity) && appConstants.value != null) {
                                    delay((appConstants.value!!.ShowSplashTime * 1000).toLong())
                                    navController.navigate("main")
                                }
                            }


                            if (!isInternetAvailable(this@MainActivity)) {
                                navController.navigate("no_internet")
                            }
                            ShowSplashScreen()
                        }

                        composable("main") {
                            LaunchedEffect(key1 = true) {
                                if (!isInternetAvailable(this@MainActivity)) {
                                    navController.navigate("no_internet")
                                }
                            }
                            if (state == null) {
                                state = rememberWebViewState(url = appConstants.value!!.EntryUrl)
                            }


                            MainScreen(
                                activity = this@MainActivity,
                                state = state!!,
                                customWebChromeClient = customWebChromeClient,
                                navigator = navigator,
                                modifier = Modifier.fillMaxSize(),
                                loaderType = appConstants.value!!.PageLoaderType,
                                enablePullRefresh = appConstants.value!!.EnablePullRefresh,
                                noInternetCB = {
                                    if (!isInternetAvailable(this@MainActivity) && navController.currentBackStackEntry?.destination?.route != "no_internet") {
                                        navController.navigate("no_internet").apply {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Please check your internet connection",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            )
                        }

                        composable("no_internet") {
                            NoInternetScreen(
                                modifier = Modifier.fillMaxSize(),
                                closeApp = { finishAndRemoveTask() },
                                onRetry = {
                                    if (isInternetAvailable(this@MainActivity)) {
                                        navController.popBackStack()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
