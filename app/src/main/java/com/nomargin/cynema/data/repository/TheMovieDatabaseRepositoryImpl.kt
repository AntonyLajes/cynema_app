package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.remote.retrofit.endpoint.TheMovieDatabaseEndpoints
import com.nomargin.cynema.data.remote.retrofit.entity.GenreResponse
import javax.inject.Inject

class TheMovieDatabaseRepositoryImpl @Inject constructor(
    private val theMovieDatabaseEndpoints: TheMovieDatabaseEndpoints
): TheMovieDatabaseRepository {
    override suspend fun getMovieGenres(): GenreResponse {
        return theMovieDatabaseEndpoints.getMovieGenres()
    }

    override suspend fun getTvShowGenres(): GenreResponse {
        return theMovieDatabaseEndpoints.getTvShowGenres()
    }
}