package com.nomargin.cynema.ui.fragment.home_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import com.nomargin.cynema.data.remote.retrofit.entity.MovieModel
import com.nomargin.cynema.data.usecase.AppLocalDatabaseUseCase
import com.nomargin.cynema.data.usecase.TheMovieDatabaseApiUseCase
import com.nomargin.cynema.util.model.CarouselModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val theMovieDatabaseApiUseCase: TheMovieDatabaseApiUseCase,
    private val appLocalDatabaseUseCase: AppLocalDatabaseUseCase
) : ViewModel() {

    private val _genres: MutableLiveData<List<GenreModel>> = MutableLiveData()
    val genres: LiveData<List<GenreModel>> = _genres
    private val _movieModelToCarouselModel: MutableLiveData<List<CarouselModel>> = MutableLiveData()
    val movieModelToCarouselModel: LiveData<List<CarouselModel>> = _movieModelToCarouselModel

    fun getGenres() = viewModelScope.launch {
        appLocalDatabaseUseCase.insertGenres(theMovieDatabaseApiUseCase.getMovieGenres().genreList)
        _genres.value = appLocalDatabaseUseCase.selectAllGenres()
    }

    private fun getMovieModelToCarouselModel(movieList: List<MovieModel>) = viewModelScope.launch{
        val movieCarouselModel: MutableList<CarouselModel> = mutableListOf()
        for(movie in movieList){
            val movieGenres: MutableList<GenreModel> = mutableListOf()
            for(movieGenre in movie.genreIds){
                val genre = async { appLocalDatabaseUseCase.selectGenreById(movieGenre) }.await()
                movieGenres.add(genre)
            }
            movieCarouselModel.add(
                CarouselModel(
                    id = movie.movieId,
                    title = movie.title,
                    backgroundPath = movie.backdropPath,
                    posterPath = movie.posterPath,
                    genres = movieGenres
                )
            )
        }
        _movieModelToCarouselModel.value = movieCarouselModel
    }

    fun getPopularMovies() = viewModelScope.launch {
        async { getMovieModelToCarouselModel(theMovieDatabaseApiUseCase.getPopularMovies().results) }.await()
    }

}