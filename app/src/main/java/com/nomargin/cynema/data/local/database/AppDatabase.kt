package com.nomargin.cynema.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nomargin.cynema.data.local.dao.AppDatabaseDao
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import com.nomargin.cynema.util.Constants

@Database(entities = [GenreModel::class], version = Constants.LOCAL_DATABASE.databaseVersion)
abstract class AppDatabase: RoomDatabase() {
    abstract fun appDatabaseDao(): AppDatabaseDao
}