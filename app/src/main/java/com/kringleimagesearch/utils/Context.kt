package com.kringleimagesearch.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.isNetworkAvailable(): Boolean {
    kotlin.runCatching {
        (applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?)?.let { conMan ->
            val capabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                conMan.getNetworkCapabilities(conMan.activeNetwork)
            } else {
                @Suppress("DEPRECATION")
                conMan.activeNetworkInfo
            }
            if (capabilities != null) {
                return true
            }
        }
    }.onFailure {
        Log.e("isNetworkAvailable", it.toString())
    }
    return false
}


fun Context.showToast(@StringRes stringResId:Int){
    Toast.makeText(this, getString(stringResId), Toast.LENGTH_SHORT).show()
}


fun Context.showToast(message:String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}