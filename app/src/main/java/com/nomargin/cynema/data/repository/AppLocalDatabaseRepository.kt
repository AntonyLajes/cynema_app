package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel

interface AppLocalDatabaseRepository {

    suspend fun insertGenres(genres: List<GenreModel>)

    suspend fun selectAllGenres(): List<GenreModel>

    suspend fun selectGenreById(genreId: Int): GenreModel

}