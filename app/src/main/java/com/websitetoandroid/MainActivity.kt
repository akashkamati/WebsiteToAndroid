package com.websitetoandroid

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
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.websitetoandroid.ui.theme.WebsiteToAndroidTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private var filePathCallback: ValueCallback<Array<Uri>>? = null
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

                    val adView = remember{
                        mutableStateOf(bannerAdView)
                    }

                    LaunchedEffect(key1 = true){
                        val adsUtil = AdsUtil(this@MainActivity)
                        if (adView.value == null && AppConstants.BannerAdId.isNotBlank()) {
                            bannerAdView = adsUtil.getBannerId(AppConstants.BannerAdId)
                            adView.value = bannerAdView
                        }
                    }


                    val navController = rememberNavController()


                    val navigator = rememberWebViewNavigator()
                    val state = rememberWebViewState(url = AppConstants.EntryUrl)

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

                                if (isMainScreen.value && state.lastLoadedUrl != null) {
                                    if (AppConstants.BottomAppBar.isNotEmpty()){
                                        BottomBar(
                                            items = AppConstants.BottomAppBar,
                                            navigator = navigator,
                                            state = state
                                        )
                                    }
                                    if (adView.value != null) {
                                        AndroidView(
                                            factory = {
                                                adView.value!!
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

                                    if (!isInternetAvailable(this@MainActivity)) {
                                        navController.navigate("no_internet")
                                    } else if (isInternetAvailable(this@MainActivity)) {
                                        delay((AppConstants.ShowSplashTime * 1000).toLong())
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
                                }


                                MainScreen(
                                    activity = this@MainActivity,
                                    state = state,
                                    customWebChromeClient = customWebChromeClient,
                                    navigator = navigator,
                                    modifier = Modifier.fillMaxSize(),
                                    loaderType = AppConstants.PageLoaderType,
                                    enablePullRefresh = AppConstants.EnablePullRefresh,
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
