package com.nomargin.cynema.ui.fragment.home_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import com.nomargin.cynema.data.remote.retrofit.entity.MovieResponse
import com.nomargin.cynema.data.usecase.AppLocalDatabaseUseCase
import com.nomargin.cynema.data.usecase.TheMovieDatabaseApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val theMovieDatabaseApiUseCase: TheMovieDatabaseApiUseCase,
    private val appLocalDatabaseUseCase: AppLocalDatabaseUseCase
) : ViewModel() {

    private val _genres: MutableLiveData<List<GenreModel>> = MutableLiveData()
    val genres: LiveData<List<GenreModel>> = _genres
    private val _popularMovies: MutableLiveData<MovieResponse> = MutableLiveData()
    val popularMovies: LiveData<MovieResponse> = _popularMovies

    fun getGenres() = viewModelScope.launch {
        appLocalDatabaseUseCase.insertGenres(theMovieDatabaseApiUseCase.getMovieGenres().genreList)
        _genres.value = appLocalDatabaseUseCase.selectAllGenres()
    }

    fun getPopularMovies() = viewModelScope.launch {
        _popularMovies.value = theMovieDatabaseApiUseCase.getPopularMovies()
    }

}