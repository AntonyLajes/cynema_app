package com.nomargin.cynema.ui.fragment.movie_details_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.remote.retrofit.entity.MovieDetailsModel
import com.nomargin.cynema.data.repository.ProfileRepository
import com.nomargin.cynema.data.repository.SharedPreferencesRepository
import com.nomargin.cynema.data.usecase.ProfileUseCase
import com.nomargin.cynema.data.usecase.TheMovieDatabaseApiUseCase
import com.nomargin.cynema.util.model.StatusModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val theMovieDatabaseApiUseCase: TheMovieDatabaseApiUseCase,
    private val profileRepository: ProfileRepository,
    private val profileUseCase: ProfileUseCase,
) : ViewModel() {

    private val _movieDetails: MutableLiveData<MovieDetailsModel> = MutableLiveData()
    val movieDetails: LiveData<MovieDetailsModel> = _movieDetails
    private var _favoriteHandlerResult: MutableLiveData<StatusModel?> = MutableLiveData()
    val favoriteHandlerResult: LiveData<StatusModel?> = _favoriteHandlerResult

    fun getDataFromSharedPreferences(key: String, defaultValue: String) = viewModelScope.launch {
        getMovieDetails(sharedPreferencesRepository.getString(key, defaultValue) ?: "")
    }

    private fun getMovieDetails(movieId: String) = viewModelScope.launch {
        _movieDetails.value = theMovieDatabaseApiUseCase.getMovieDetails(movieId)
    }

    fun addMovieToFavorites() = viewModelScope.launch {
        movieDetails.value?.id.let {
            _favoriteHandlerResult.value =
                profileRepository.handlerFavoriteMovies(it.toString()).statusModel
        }
    }

    fun getUserData() = viewModelScope.launch {
        movieDetails.value?.id.let {
            _favoriteHandlerResult.value =
                profileUseCase.verifyIfMovieIsFavorite(it.toString())
        }
    }

}