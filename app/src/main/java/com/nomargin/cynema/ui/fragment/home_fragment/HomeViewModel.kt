package com.nomargin.cynema.ui.fragment.home_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.remote.retrofit.entity.GenreResponse
import com.nomargin.cynema.data.remote.retrofit.entity.MovieResponse
import com.nomargin.cynema.data.usecase.TheMovieDatabaseApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val theMovieDatabaseApiUseCase: TheMovieDatabaseApiUseCase
) : ViewModel() {

    private val _genres: MutableLiveData<GenreResponse> = MutableLiveData()
    val genres: LiveData<GenreResponse> = _genres
    private val _popularMovies: MutableLiveData<MovieResponse> = MutableLiveData()
    val popularMovies: LiveData<MovieResponse> = _popularMovies

    fun getGenres() = viewModelScope.launch {
        _genres.value = theMovieDatabaseApiUseCase.getMovieGenres()
    }

    fun getPopularMovies() = viewModelScope.launch {
        _popularMovies.value = theMovieDatabaseApiUseCase.getPopularMovies()
    }

}