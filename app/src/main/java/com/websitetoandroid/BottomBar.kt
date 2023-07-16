package com.websitetoandroid

import android.content.Context
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.toAndroidIconCompat
import com.websitetoandroid.data.BottomBarItem
import java.lang.Exception


@Composable
fun BottomBar(context: Context, items:List<BottomBarItem>, navigator: WebViewNavigator, state:WebViewState) {

    NavigationBar{

        items.forEach {
            NavigationBarItem(
                selected = state.lastLoadedUrl!! == it.url , 
                onClick = { navigator.loadUrl(it.url) },
                icon = {
                    try {
                        IconicsDrawable(context,
                            FontAwesome.Icon.valueOf(it.icon)).apply {
                            //  sizeDp = iconSize.toInt()
                            //  color = IconicsColor.colorInt(Color.parseColor("#FF0000"))
                        }.toAndroidIconCompat()
                    }catch (e:Exception){
                        e.printStackTrace()

                    }

                },
                label = {
                    Text(text = it.title)
                }
            )
            

        }
    }



}