package com.nomargin.cynema.data.di

import com.nomargin.cynema.data.repository.SharedPreferencesRepository
import com.nomargin.cynema.data.repository.SharedPreferencesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SharedPreferencesRepositoryModule {

    @Binds
    abstract fun bindsSharedPreferencesRepositoryImpl(
        sharedPreferencesRepositoryImpl: SharedPreferencesRepositoryImpl
    ): SharedPreferencesRepository

}