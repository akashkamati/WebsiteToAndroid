package com.websitetoandroid.data

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object DataUtil {

    suspend fun getAppConstants(context: Context) : AppConstants? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.assets.open("appConstants.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val jsonString = String(buffer)
            val gson = Gson()
            gson.fromJson(jsonString, AppConstants::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}