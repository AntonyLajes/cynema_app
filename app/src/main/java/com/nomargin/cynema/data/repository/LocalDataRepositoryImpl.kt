package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.local.dao.AppDatabaseDao
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import javax.inject.Inject

class LocalDataRepositoryImpl @Inject constructor(
    private val appDatabaseDao: AppDatabaseDao
): LocalDataRepository {
    override suspend fun insert(genres: List<GenreModel>) {
        appDatabaseDao.insert(genres)
    }

    override suspend fun getAll(): List<GenreModel> {
        return appDatabaseDao.getAllGenres()
    }

    override suspend fun getAllGenres(genreId: Int): GenreModel {
        return appDatabaseDao.getGenre(genreId)
    }
}