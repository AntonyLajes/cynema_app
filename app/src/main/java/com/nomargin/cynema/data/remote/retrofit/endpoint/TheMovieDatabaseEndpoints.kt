package com.nomargin.cynema.data.remote.retrofit.endpoint

import com.nomargin.cynema.data.remote.retrofit.entity.GenreResponse
import retrofit2.http.GET

interface TheMovieDatabaseEndpoints {

    @GET("genre/movie/list?language=en")
    suspend fun getMovieGenres(): GenreResponse

    @GET("genre/tv/list?language=en")
    suspend fun getTvShowGenres(): GenreResponse

}