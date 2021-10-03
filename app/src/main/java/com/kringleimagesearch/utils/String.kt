package com.kringleimagesearch.utils

fun String.toSqlLikeQueryValue() = "%$this%"