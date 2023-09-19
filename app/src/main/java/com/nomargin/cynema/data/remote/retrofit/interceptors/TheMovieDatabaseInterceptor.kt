package com.nomargin.cynema.data.remote.retrofit.interceptors

import com.nomargin.cynema.util.Private
import okhttp3.Interceptor
import okhttp3.Response

class TheMovieDatabaseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
            .newBuilder()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", Private.theMovieDatabaseHeaderAuthorization)
            .build()

        return chain.proceed(request)
    }

}