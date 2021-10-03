@file:Suppress("unused")

package com.kringleimagesearch.data.remote

import com.squareup.moshi.Json

enum class Status{
    LOADING,ERROR,SUCCESS
}

/**
 * A generic class to store the response.
 * It keeps track of common variable like status, message, responseCode
 * and custom data which we receive from different APIs as data variable.
 */
open class BaseResponse<T> {

    var status:Status=Status.LOADING

    @Json(name = "totalEstimatedMatches")
    var totalEstimatedMatches:Int=0

    @Json(name = "value")
    var data: T? = null

    var message: String=""

    var responseCode:Int=0

    companion object {
        fun <T> getErrorResponse(responseCode:Int= -100, message: String): BaseResponse<T> {
            val baseResponse = BaseResponse<T>()
            baseResponse.status=Status.ERROR
            baseResponse.message = message
            baseResponse.responseCode=responseCode
            return baseResponse
        }

        fun <T> getSuccessResponse(data:T?,message: String="success"):BaseResponse<T>{
            val baseResponse = BaseResponse<T>()
            baseResponse.status=Status.SUCCESS
            baseResponse.message = message
            baseResponse.responseCode= 200
            baseResponse.data= data
            return baseResponse
        }

        fun <T> getLoadingResponse(): BaseResponse<T> {
            val baseResponse = BaseResponse<T>()
            baseResponse.status=Status.LOADING
            return baseResponse
        }
    }
}


fun <T1,T2> BaseResponse<T1>.convertTo(target:T2):BaseResponse<T2>{
    val newBaseResponse=BaseResponse<T2>()
    newBaseResponse.status=status
    newBaseResponse.responseCode=responseCode
    newBaseResponse.message=message
    newBaseResponse.data=target
    return newBaseResponse
}