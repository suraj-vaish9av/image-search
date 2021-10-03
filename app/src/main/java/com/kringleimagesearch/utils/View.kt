package com.kringleimagesearch.utils

import android.view.View
import kotlinx.coroutines.*

fun View.onClick(throttleDelay:Long=500L,perform:(view:View)->Unit){

    val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    var job: Job?=null

    setOnClickListener(View.OnClickListener {
        if (job?.isActive == true)
            return@OnClickListener
        job= uiScope.launch {
            perform(it)
            delay(throttleDelay)
        }
    })
}
