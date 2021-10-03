package com.kringleimagesearch.utils

import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.onSearch(block:()->Unit){
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            block.invoke()
            return@setOnEditorActionListener true
        }
        false
    }
}