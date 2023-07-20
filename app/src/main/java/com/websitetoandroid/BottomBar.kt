package com.websitetoandroid

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState
import com.websitetoandroid.data.BottomBarItem


@Composable
fun BottomBar(items: List<BottomBarItem>, navigator: WebViewNavigator, state: WebViewState) {

    NavigationBar {

        items.forEach {
            NavigationBarItem(
                selected = state.lastLoadedUrl!! == it.url,
                onClick = { navigator.loadUrl(it.url) },
                icon = {
                    Icon(imageVector = it.icon, "")
                },
                label = {
                    Text(text = it.title)
                }
            )
        }
    }
}