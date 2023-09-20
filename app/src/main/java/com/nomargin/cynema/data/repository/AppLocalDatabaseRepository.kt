package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.local.entity.Genre

interface AppLocalDatabaseRepository {

    suspend fun insertGenres(genres: List<Genre>)

    suspend fun selectAllGenres(): List<Genre>

    suspend fun selectGenreById(genreId: Int): List<Genre>

}