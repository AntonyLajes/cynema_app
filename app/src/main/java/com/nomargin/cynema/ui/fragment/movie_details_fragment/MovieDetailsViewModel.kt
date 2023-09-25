package com.nomargin.cynema.ui.fragment.movie_details_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.remote.retrofit.entity.MovieDetailsModel
import com.nomargin.cynema.data.repository.SharedPreferencesRepository
import com.nomargin.cynema.data.usecase.TheMovieDatabaseApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val theMovieDatabaseApiUseCase: TheMovieDatabaseApiUseCase
) : ViewModel() {

    private val _movieDetails: MutableLiveData<MovieDetailsModel> = MutableLiveData()
    val movieDetails: LiveData<MovieDetailsModel> = _movieDetails

    fun getDataFromSharedPreferences(key: String, defaultValue: String) = viewModelScope.launch {
        getMovieDetails(sharedPreferencesRepository.getString(key, defaultValue) ?: "")
    }

    private fun getMovieDetails(movieId: String) = viewModelScope.launch {
        _movieDetails.value = theMovieDatabaseApiUseCase.getMovieDetails(movieId)
    }

}