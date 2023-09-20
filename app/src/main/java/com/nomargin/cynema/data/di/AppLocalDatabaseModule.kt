package com.nomargin.cynema.data.di

import android.content.Context
import androidx.room.Room
import com.nomargin.cynema.data.local.dao.AppLocalDatabaseDao
import com.nomargin.cynema.data.local.database.AppLocalDatabase
import com.nomargin.cynema.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppLocalDatabaseModule {

    @Provides
    fun providesAppLocalDatabaseDao(
        appLocalDatabase: AppLocalDatabase
    ): AppLocalDatabaseDao {
        return appLocalDatabase.appLocalDatabaseDao()
    }

    @Provides
    @Singleton
    fun providesAppLocalDatabase(
        @ApplicationContext
        context: Context
    ): AppLocalDatabase {
        return Room.databaseBuilder(
            context,
            AppLocalDatabase::class.java,
            Constants.LOCAL_STORAGE.databaseName
        ).build()
    }

}