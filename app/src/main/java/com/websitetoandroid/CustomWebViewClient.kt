package com.websitetoandroid

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.Toast
import com.google.accompanist.web.AccompanistWebViewClient

class CustomWebViewClient(private val activity: Activity,private val noInternetCB : () -> Unit) : AccompanistWebViewClient(){

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

        if (isInternetAvailable(activity)) {
            val url = request?.url.toString()
            if (!url.startsWith("https") || !url.startsWith("http")) {
                Intent(Intent.ACTION_VIEW).apply {
                    Uri.parse(request?.url.toString())
                    activity.startActivity(this)
                }
            } else if (url.startsWith("tel:")) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse(url)
                activity.startActivity(intent)
            } else if (url.startsWith("mailto:")) {
                try {
                    Intent(Intent.ACTION_SENDTO, Uri.parse(url)).apply {
                        activity.startActivity(this)
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        activity,
                        "No apps available to handle mails",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else if (url.startsWith("https://wa.me/") || url.startsWith("whatsapp://send/")) {
                return try {
                    Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        activity.startActivity(this)
                    }
                    true
                } catch (e: Exception) {
                    Toast.makeText(
                        activity,
                        "No apps available to handle the request",
                        Toast.LENGTH_LONG
                    ).show()
                    false
                }
            } else {
                view?.loadUrl(request?.url.toString())
            }
            return true
        }else{
            noInternetCB()
            return false
        }


    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        println("some error occurred ${errorResponse?.statusCode}")
        if (errorResponse?.statusCode == ERROR_CONNECT){
            noInternetCB()
        }
    }

    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        if (error?.description?.toString() == "net::ERR_INTERNET_DISCONNECTED"){
            noInternetCB()
        }
        println("the error is ${error?.description} ${error?.errorCode}")
    }

}

