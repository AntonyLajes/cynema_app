package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.local.entity.MovieSearchedAppearanceModel
import com.nomargin.cynema.data.remote.retrofit.entity.GenreResponse
import com.nomargin.cynema.data.remote.retrofit.entity.MovieDetailsModel
import com.nomargin.cynema.data.remote.retrofit.entity.MovieResponse

interface TheMovieDatabaseApiUseCase {
    suspend fun getMovieGenres(): GenreResponse
    suspend fun getTvShowGenres(): GenreResponse
    suspend fun getPopularMovies(): MovieResponse
    suspend fun getNowPlayingMovies(): MovieResponse
    suspend fun getTopRatedMovies(): MovieResponse
    suspend fun getUpcomingMovies(): MovieResponse
    suspend fun getMovieDetails(movieId: String): MovieDetailsModel
    suspend fun getPopularMoviesToSearchFragment(): MovieSearchedAppearanceModel
    suspend fun getMovieDetailsByQuery(query: String): MovieSearchedAppearanceModel
}