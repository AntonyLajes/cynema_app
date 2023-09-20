package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.remote.retrofit.entity.GenreResponse
import com.nomargin.cynema.data.remote.retrofit.entity.MovieResponse

interface TheMovieDatabaseRepository {

    suspend fun getMovieGenres(): GenreResponse

    suspend fun getTvShowGenres(): GenreResponse

    suspend fun getPopularMovies(): MovieResponse
    suspend fun getNowPlayingMovies(): MovieResponse
    suspend fun getTopRatedMovies(): MovieResponse
    suspend fun getUpcomingMovies(): MovieResponse

}