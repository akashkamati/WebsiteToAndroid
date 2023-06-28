package com.websitetoandroid

import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient

class CustomWebChromeClient : WebChromeClient() {

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {

        return true
    }
}