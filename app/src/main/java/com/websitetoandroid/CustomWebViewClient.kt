package com.websitetoandroid

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class CustomWebViewClient(private val activity: Activity) : WebViewClient(){

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

        if (!request?.url.toString().startsWith("https")){
            Intent(Intent.ACTION_VIEW).apply {
                Uri.parse(request?.url.toString())
                activity.startActivity(this)
            }
        } else{
            view?.loadUrl(request?.url.toString())
        }

        return super.shouldOverrideUrlLoading(view, request)
    }


    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
    }
}

