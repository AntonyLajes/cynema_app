package com.nomargin.cynema.data.remote.retrofit.endpoint

import com.nomargin.cynema.data.remote.retrofit.entity.GenreResponse
import com.nomargin.cynema.data.remote.retrofit.entity.MovieResponse
import retrofit2.http.GET

interface TheMovieDatabaseEndpoints {

    @GET("genre/movie/list?language=en")
    suspend fun getMovieGenres(): GenreResponse

    @GET("genre/tv/list?language=en")
    suspend fun getTvShowGenres(): GenreResponse

    @GET("movie/popular?language=en-US")
    suspend fun getPopularMovies(): MovieResponse

    @GET("movie/now_playing?language=en-US")
    suspend fun getNowPlayingMovies(): MovieResponse

    @GET("movie/top_rated?language=en-US")
    suspend fun getTopRatedMovies(): MovieResponse

    @GET("movie/upcoming?language=en-US")
    suspend fun getUpcomingMovies(): MovieResponse

}