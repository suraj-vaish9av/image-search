package com.kringleimagesearch.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kringleimagesearch.R

fun Activity.hideKeyboard(fragView: View?=null){

    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view: View? = currentFocus
    if (view == null) {
        view = if (fragView!=null)
            fragView.rootView
        else
            View(this)
    }
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
    view?.clearFocus()
}


fun AppCompatActivity.setupToolbar(title: String, backStatus: Boolean) {
    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.setDisplayHomeAsUpEnabled(backStatus)
    supportActionBar?.setDisplayShowTitleEnabled(false)
    toolbar.title = title
}