package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.local.dao.AppLocalDatabaseDao
import com.nomargin.cynema.data.local.entity.Genre
import javax.inject.Inject

class AppLocalDatabaseRepositoryImpl @Inject constructor(
    private val appLocalDatabaseDao: AppLocalDatabaseDao
): AppLocalDatabaseRepository {
    override suspend fun insertGenres(genres: List<Genre>) {
        appLocalDatabaseDao.insertGenres(genres)
    }

    override suspend fun selectAllGenres(): List<Genre> {
        return appLocalDatabaseDao.selectAllGenres()
    }

    override suspend fun selectGenreById(genreId: Int): List<Genre> {
        return appLocalDatabaseDao.selectGenreById(genreId)
    }
}