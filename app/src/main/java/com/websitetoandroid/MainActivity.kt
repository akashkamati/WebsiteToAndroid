package com.websitetoandroid

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.ValueCallback
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
    private var bannerAdView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            WebsiteToAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorScheme.background
                ) {
                    val navController = rememberNavController()


                    val appConstants: MutableState<AppConstants?> = remember {
                        mutableStateOf(null)
                    }
                    val navigator = rememberWebViewNavigator()

                    val isMainScreen = rememberSaveable {
                        mutableStateOf(false)
                    }
                    val currentBackStackEntry = navController.currentBackStackEntryAsState().value

                    SideEffect {
                        when (currentBackStackEntry?.destination?.route) {
                            "main_screen" -> {
                                isMainScreen.value = true
                            }
                            else -> {
                                isMainScreen.value = false
                            }
                        }
                    }


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

                    Scaffold(
                        bottomBar = {
                            Column {
                                if (isMainScreen.value && state?.isLoading != true && state?.lastLoadedUrl != null) {
                                    if (bannerAdView != null) {
                                        AndroidView(
                                            factory = {
                                                bannerAdView!!
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }

                        }
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "splash",
                            modifier = Modifier.padding(it)
                        ) {

                            composable("splash") {
                                LaunchedEffect(key1 = true) {
                                    val adsUtil = AdsUtil(this@MainActivity)

                                    if (!isInternetAvailable(this@MainActivity)) {
                                        navController.navigate("no_internet")
                                    } else if (isInternetAvailable(this@MainActivity) && appConstants.value != null) {
                                        bannerAdView =
                                            adsUtil.getBannerId(appConstants.value!!.BannerAdId)
                                        delay((appConstants.value!!.ShowSplashTime * 1000).toLong())
                                        navController.navigate("main_screen")
                                    }
                                }


                                if (!isInternetAvailable(this@MainActivity)) {
                                    navController.navigate("no_internet")
                                }
                                ShowSplashScreen()
                            }

                            composable("main_screen") {
                                LaunchedEffect(key1 = true) {
                                    if (!isInternetAvailable(this@MainActivity)) {
                                        navController.navigate("no_internet")
                                    }
                                    if (appConstants.value!!.BannerAdId.isNotBlank() && bannerAdView == null) {
                                        val adsUtil = AdsUtil(this@MainActivity)
                                        bannerAdView =
                                            adsUtil.getBannerId(appConstants.value!!.BannerAdId)
                                    }
                                }
                                if (state == null) {
                                    state =
                                        rememberWebViewState(url = appConstants.value!!.EntryUrl)
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
}
