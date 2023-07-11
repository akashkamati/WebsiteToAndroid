package com.websitetoandroid

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.google.accompanist.web.AccompanistWebViewClient

class CustomWebViewClient(private val activity: Activity) : AccompanistWebViewClient(){

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

        if (!request?.url.toString().startsWith("https")){
            Intent(Intent.ACTION_VIEW).apply {
                Uri.parse(request?.url.toString())
                activity.startActivity(this)
            }
        } else{
            view?.loadUrl(request?.url.toString())
        }

        return true
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {

        println("some error occurred")
        return
    }

    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        println("the error is ${error?.description} ${error?.errorCode}")
        return
    }

}

