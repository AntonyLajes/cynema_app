package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.remote.retrofit.entity.GenreResponse

interface TheMovieDatabaseRepository {

    suspend fun getMovieGenres(): GenreResponse

    suspend fun getTvShowGenres(): GenreResponse

}