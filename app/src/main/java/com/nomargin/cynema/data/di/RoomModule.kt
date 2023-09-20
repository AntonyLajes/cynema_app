package com.nomargin.cynema.data.di

import android.content.Context
import androidx.room.Room
import com.nomargin.cynema.data.local.dao.AppDatabaseDao
import com.nomargin.cynema.data.local.database.AppDatabase
import com.nomargin.cynema.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    fun providesAppDatabaseDao(
        appDatabase: AppDatabase
    ): AppDatabaseDao {
        return appDatabase.appDatabaseDao()
    }
    @Provides
    fun providesAppDatabase(
        @ApplicationContext
        context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            Constants.LOCAL_DATABASE.databaseName
        ).build()
    }

}