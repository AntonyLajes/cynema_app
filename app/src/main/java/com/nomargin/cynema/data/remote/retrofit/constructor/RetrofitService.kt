package com.nomargin.cynema.data.remote.retrofit.constructor

import com.nomargin.cynema.data.remote.retrofit.interceptors.TheMovieDatabaseInterceptor
import com.nomargin.cynema.util.Private
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    private val client = OkHttpClient.Builder().apply {
        connectTimeout(60, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
        addInterceptor(TheMovieDatabaseInterceptor())
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Private.theMovieDatabaseBaseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <API> createRetrofitService(apiClass: Class<API>): API = retrofit.create(apiClass)

}