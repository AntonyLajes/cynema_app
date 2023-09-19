package com.nomargin.cynema.ui.fragment.home_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.remote.retrofit.entity.GenreResponse
import com.nomargin.cynema.data.usecase.TheMovieDatabaseApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val theMovieDatabaseApiUseCase: TheMovieDatabaseApiUseCase
) : ViewModel() {

    val _genres: MutableLiveData<GenreResponse> = MutableLiveData()

    fun getGenres() = viewModelScope.launch {
        _genres.value = theMovieDatabaseApiUseCase.getMovieGenres()
    }

}