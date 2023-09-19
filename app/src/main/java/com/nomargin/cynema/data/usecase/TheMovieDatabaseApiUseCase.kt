package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.remote.retrofit.entity.GenreResponse

interface TheMovieDatabaseApiUseCase {
    suspend fun getMovieGenres(): GenreResponse
    suspend fun getTvShowGenres(): GenreResponse
}