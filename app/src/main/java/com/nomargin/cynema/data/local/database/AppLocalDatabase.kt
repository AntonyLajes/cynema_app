package com.nomargin.cynema.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nomargin.cynema.data.local.dao.AppLocalDatabaseDao
import com.nomargin.cynema.data.local.entity.Genre
import com.nomargin.cynema.util.Constants

@Database(entities = [Genre::class], version = Constants.LOCAL_STORAGE.databaseVersion)
abstract class AppLocalDatabase: RoomDatabase() {

    abstract fun appLocalDatabaseDao(): AppLocalDatabaseDao

}