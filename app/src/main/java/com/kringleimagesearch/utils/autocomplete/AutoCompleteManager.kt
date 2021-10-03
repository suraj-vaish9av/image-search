package com.kringleimagesearch.utils.autocomplete

import android.widget.AutoCompleteTextView
import com.kringleimagesearch.R
import com.kringleimagesearch.data.database.entities.Suggestion

class AutoCompleteManager {

    fun setupAutoCompleteAdapter(autoCompleteTextView: AutoCompleteTextView, list: List<Suggestion>){
        val adapter= AutoCompleteAdapter(
            autoCompleteTextView.context,
            R.layout.item_autocomplete,
            list
        )
        autoCompleteTextView.threshold=1
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener { _, _, pos, _ ->
            autoCompleteTextView.requestFocus()
            autoCompleteTextView.setSelection(autoCompleteTextView.text.length)
        }
    }
}