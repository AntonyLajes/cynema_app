package com.nomargin.cynema.data.di

import com.nomargin.cynema.data.remote.retrofit.constructor.RetrofitService
import com.nomargin.cynema.data.remote.retrofit.endpoint.TheMovieDatabaseEndpoints
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun providesRetrofitService(): RetrofitService = RetrofitService

    @Provides
    @Singleton
    fun providesTheMovieDatabaseEndpoints(
        retrofitService: RetrofitService
    ): TheMovieDatabaseEndpoints {
        return retrofitService.createRetrofitService(TheMovieDatabaseEndpoints::class.java)
    }

}