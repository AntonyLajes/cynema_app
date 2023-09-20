package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel

interface AppLocalDatabaseUseCase {
    suspend fun insertGenres(genres: List<GenreModel>)
    suspend fun selectAllGenres(): List<GenreModel>
    suspend fun selectGenreById(genreId: Int): GenreModel

}