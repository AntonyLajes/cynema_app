package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.remote.retrofit.entity.GenreResponse
import com.nomargin.cynema.data.remote.retrofit.entity.MovieResponse
import com.nomargin.cynema.data.repository.TheMovieDatabaseRepository
import javax.inject.Inject

class TheMovieDatabaseApiUseCaseImpl @Inject constructor(
    private val theMovieDatabaseRepository: TheMovieDatabaseRepository
): TheMovieDatabaseApiUseCase {
    override suspend fun getMovieGenres(): GenreResponse {
        return theMovieDatabaseRepository.getMovieGenres()
    }

    override suspend fun getTvShowGenres(): GenreResponse {
        return theMovieDatabaseRepository.getTvShowGenres()
    }

    override suspend fun getPopularMovies(): MovieResponse {
        return theMovieDatabaseRepository.getPopularMovies()
    }

    override suspend fun getNowPlayingMovies(): MovieResponse {
        return theMovieDatabaseRepository.getNowPlayingMovies()
    }

    override suspend fun getTopRatedMovies(): MovieResponse {
        return theMovieDatabaseRepository.getTopRatedMovies()
    }

    override suspend fun getUpcomingMovies(): MovieResponse {
        return theMovieDatabaseRepository.getUpcomingMovies()
    }
}