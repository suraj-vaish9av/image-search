package com.kringleimagesearch.data.remote.services

import com.kringleimagesearch.data.database.entities.ImageResultModel
import com.kringleimagesearch.data.remote.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageSearchService {

    @GET("images/search")
    suspend fun searchImagesAsync(@Query("q") query:String,
                                  @Query("offset") offset:Int,
                                  @Query("count") count:Int):Response<BaseResponse<List<ImageResultModel>>>
}