package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.local.dao.AppLocalDatabaseDao
import com.nomargin.cynema.data.local.database.AppLocalDatabase
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import javax.inject.Inject

class AppLocalDatabaseRepositoryImpl @Inject constructor(
    private val appLocalDatabaseDao: AppLocalDatabaseDao,
    private val appLocalDatabase: AppLocalDatabase,
) : AppLocalDatabaseRepository {
    override suspend fun insertGenres(genres: List<GenreModel>) {
        appLocalDatabaseDao.insertGenres(genres)
    }

    override suspend fun selectAllGenres(): List<GenreModel> {
        return appLocalDatabaseDao.selectAllGenres()
    }

    override suspend fun selectGenreById(genreId: Int): GenreModel {
        return appLocalDatabaseDao.selectGenreById(genreId)
    }

    override suspend fun closeDatabase() {
        appLocalDatabase.close()
    }
}