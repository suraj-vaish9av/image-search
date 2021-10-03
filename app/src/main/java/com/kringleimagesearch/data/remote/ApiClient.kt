package com.kringleimagesearch.data.remote

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kringleimagesearch.BuildConfig
import com.kringleimagesearch.data.database.ImageSearchDatabase
import com.kringleimagesearch.data.remote.repositories.ImageSearchRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit client.
 */
object ApiClient {

    private var retrofit: Retrofit? = null
    private const val REQUEST_TIME_OUT = 10L
    private var okHttpClient: OkHttpClient? = null


    private fun getClient(): Retrofit {
        if (okHttpClient == null)
            initOkHttp()
        if (retrofit == null) {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient!!)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        return retrofit!!
    }

    private fun initOkHttp() {

        val httpClient=
            OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(loggingInterceptor)



        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader("x-rapidapi-key",BuildConfig.IMAGE_SEARCH_API_KEY)
                .addHeader("x-rapidapi-host",BuildConfig.RAPID_API_HOST)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        okHttpClient = httpClient.build()
    }

   private inline fun <reified T> getService() = getClient().create(T::class.java)

    fun getImageSearchRepository(context: Context) = ImageSearchRepository(getService(),ImageSearchDatabase.getInstance(context))
}
