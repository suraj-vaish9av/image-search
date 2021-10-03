package com.kringleimagesearch.data.remote.repositories

import android.content.Context
import com.kringleimagesearch.R
import com.kringleimagesearch.data.remote.BaseResponse
import com.kringleimagesearch.utils.isNetworkAvailable
import retrofit2.Response

open class BaseRepository {

    companion object{
        const val RC_BAD_REQUEST=400
        const val RC_RESOURCE_NOT_FOUND=404
        const val RC_UNAUTHORIZED=401
        const val RC_FORBIDDEN=403
        const val RC_METHOD_NOT_ALLOWED=405
        const val RC_INTERNAL_SERVER_ERROR=500
        const val RC_SUCCESS=200
        const val RC_UNKNOWN_ERROR=-100
    }

    suspend fun<T:Any> safeApiCall(context: Context, block: suspend ()->Response<BaseResponse<T>>, error:String): BaseResponse<T> {

        if (!context.isNetworkAvailable())
            return BaseResponse.getErrorResponse(message = context.getString(R.string.no_internet))

        return try {
            val response=block()
            val baseResponse = response.body()
            if (response.isSuccessful && response.code()== RC_SUCCESS && baseResponse!=null) {
                baseResponse
            } else{
                BaseResponse.getErrorResponse(response.code(), error)
            }
        }catch (e:Exception){
            BaseResponse.getErrorResponse(RC_UNKNOWN_ERROR, error)
        }
    }
}
