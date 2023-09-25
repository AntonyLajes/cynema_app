package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.remote.retrofit.endpoint.TheMovieDatabaseEndpoints
import com.nomargin.cynema.data.remote.retrofit.entity.GenreResponse
import com.nomargin.cynema.data.remote.retrofit.entity.MovieDetailsModel
import com.nomargin.cynema.data.remote.retrofit.entity.MovieResponse
import javax.inject.Inject

class TheMovieDatabaseRepositoryImpl @Inject constructor(
    private val theMovieDatabaseEndpoints: TheMovieDatabaseEndpoints
) : TheMovieDatabaseRepository {
    override suspend fun getMovieGenres(): GenreResponse {
        return theMovieDatabaseEndpoints.getMovieGenres()
    }

    override suspend fun getTvShowGenres(): GenreResponse {
        return theMovieDatabaseEndpoints.getTvShowGenres()
    }

    override suspend fun getPopularMovies(): MovieResponse {
        return theMovieDatabaseEndpoints.getPopularMovies()
    }

    override suspend fun getNowPlayingMovies(): MovieResponse {
        return theMovieDatabaseEndpoints.getNowPlayingMovies()
    }

    override suspend fun getTopRatedMovies(): MovieResponse {
        return theMovieDatabaseEndpoints.getTopRatedMovies()
    }

    override suspend fun getUpcomingMovies(): MovieResponse {
        return theMovieDatabaseEndpoints.getUpcomingMovies()
    }

    override suspend fun getMovieDetails(movieId: String): MovieDetailsModel {
        return theMovieDatabaseEndpoints.getMovieDetails(movieId)
    }
}