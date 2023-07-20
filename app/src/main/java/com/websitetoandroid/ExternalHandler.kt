package com.websitetoandroid

fun shouldOpenExternally(url:String) : Boolean{
    var flag = false
    AppConstants.OpenExternally.forEach out@{
        if (url.contains(it)){
            flag = true
            return@out
        }
    }
    return flag
}