package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.local.entity.MovieSearchedAppearanceModel
import com.nomargin.cynema.data.local.entity.MovieSearchedDetailsModel
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import com.nomargin.cynema.data.remote.retrofit.entity.GenreResponse
import com.nomargin.cynema.data.remote.retrofit.entity.MovieDetailsModel
import com.nomargin.cynema.data.remote.retrofit.entity.MovieModel
import com.nomargin.cynema.data.remote.retrofit.entity.MovieResponse
import com.nomargin.cynema.data.repository.TheMovieDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TheMovieDatabaseApiUseCaseImpl @Inject constructor(
    private val theMovieDatabaseRepository: TheMovieDatabaseRepository,
    private val appLocalDatabaseUseCase: AppLocalDatabaseUseCase,
) : TheMovieDatabaseApiUseCase {
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

    override suspend fun getMovieDetails(movieId: String): MovieDetailsModel {
        val movieDetails = withContext(Dispatchers.IO) {
            theMovieDatabaseRepository.getMovieDetails(movieId)
        }
        return movieDetails
    }

    override suspend fun getMovieDetailsByQuery(query: String): MovieSearchedAppearanceModel {
        val movieResponse = theMovieDatabaseRepository.getMovieDetailsByQuery(query)
        return MovieSearchedAppearanceModel(
            pageNumber = movieResponse.pageNumber,
            results = changeMovieDetailsModelToMovieSearchedDetailsModel(movieResponse.results),
            totalPages = movieResponse.totalPages,
            totalResults = movieResponse.totalResults
        )
    }

    override suspend fun getPopularMoviesToSearchFragment(): MovieSearchedAppearanceModel {
        val movieResponse = getPopularMovies()
        return MovieSearchedAppearanceModel(
            pageNumber = movieResponse.pageNumber,
            results = changeMovieDetailsModelToMovieSearchedDetailsModel(movieResponse.results),
            totalPages = movieResponse.totalPages,
            totalResults = movieResponse.totalResults
        )
    }

    private suspend fun changeMovieDetailsModelToMovieSearchedDetailsModel(results: List<MovieModel>): List<MovieSearchedDetailsModel> {
        val movieSearchedDetailsModel: MutableList<MovieSearchedDetailsModel> = mutableListOf()
        for (result in results) {
            val movieGenres: MutableList<GenreModel> = mutableListOf()
            for (movieGenre in result.genreIds) {
                val genre = appLocalDatabaseUseCase.selectGenreById(movieGenre)
                movieGenres.add(genre)
            }
            movieSearchedDetailsModel.add(
                MovieSearchedDetailsModel(
                    result.movieId,
                    result.title,
                    result.backdropPath,
                    result.posterPath,
                    result.releaseDate,
                    movieGenres
                )
            )
        }
        return movieSearchedDetailsModel
    }

    override suspend fun getMovieDetailsById(movieId: String): MovieSearchedDetailsModel {
        val movieResponse = theMovieDatabaseRepository.getMovieDetails(movieId)
        return MovieSearchedDetailsModel(
            id = movieResponse.id,
            title = movieResponse.title,
            backgroundPath = movieResponse.backdropPath,
            posterPath = movieResponse.posterPath,
            releaseDate = movieResponse.releaseDate,
            genres = movieResponse.genres
        )
    }
}