package com.nomargin.cynema.data.di

import android.content.Context
import android.content.SharedPreferences
import com.nomargin.cynema.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(
        @ApplicationContext
        context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            Constants.LOCAL_STORAGE.sharedPreferencesGeneralKey,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun providesSharedPreferencesEditor(
        sharedPreferences: SharedPreferences
    ): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }
}