package com.nomargin.cynema.ui.fragment.favorites_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.local.entity.MovieSearchedDetailsModel
import com.nomargin.cynema.data.repository.SharedPreferencesRepository
import com.nomargin.cynema.data.usecase.ProfileUseCase
import com.nomargin.cynema.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : ViewModel() {

    private var _moviesAddedInFavoriteResult: MutableLiveData<Resource<List<MovieSearchedDetailsModel>>> =
        MutableLiveData()
    val moviesAddedInFavoriteResult: LiveData<Resource<List<MovieSearchedDetailsModel>>> =
        _moviesAddedInFavoriteResult

    fun getFavoriteMovies() = viewModelScope.launch {
        _moviesAddedInFavoriteResult.value = profileUseCase.getFavoriteMovies()
    }

    fun saveDataToSharedPreferences(key: String, movieId: String){
        sharedPreferencesRepository.putString(key, movieId)
    }

}