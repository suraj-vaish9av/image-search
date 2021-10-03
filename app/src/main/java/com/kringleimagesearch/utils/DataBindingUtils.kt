package com.kringleimagesearch.utils

import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.kringleimagesearch.data.database.entities.Suggestion
import com.kringleimagesearch.utils.autocomplete.AutoCompleteManager

@BindingAdapter("image")
fun ImageView.setImage(path:String){
    GlideApp.with(this).load(path).into(this)
}

@BindingAdapter("suggestions")
fun AutoCompleteTextView.setSuggestions(list: List<Suggestion>?){
    list?.let {
        val autoCompleteManager= AutoCompleteManager()
        autoCompleteManager.setupAutoCompleteAdapter(this,list)
    }
}