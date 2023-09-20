package com.nomargin.cynema.data.di

import com.nomargin.cynema.data.repository.LocalDataRepository
import com.nomargin.cynema.data.repository.LocalDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ApiModule {

    @Binds
    abstract fun bindsLocalDataRepository(
        localDataRepositoryImpl: LocalDataRepositoryImpl
    ): LocalDataRepository

}