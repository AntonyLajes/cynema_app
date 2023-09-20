package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel

interface LocalDataRepository {
    suspend fun insert(genres: List<GenreModel>)
    suspend fun getAll(): List<GenreModel>
    suspend fun getAllGenres(genreId: Int): GenreModel

}