package com.websitetoandroid

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient.FileChooserParams
import androidx.activity.result.contract.ActivityResultContract

class FileChooserContract : ActivityResultContract<FileChooserParams, List<Uri>?>() {
    override fun createIntent(context: Context, input: FileChooserParams): Intent {

        return Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*" // Set the MIME type according to your requirement
        }

//
//        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//            addCategory(Intent.CATEGORY_OPENABLE)
//            type = "*/*" // Set the MIME type according to your requirement
//        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri>? {
        if (resultCode == android.app.Activity.RESULT_OK && intent != null && intent.data != null) {
            return arrayListOf(intent.data!!)
        }
        return null
    }
}
