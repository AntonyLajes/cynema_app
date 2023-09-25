package com.nomargin.cynema.ui.fragment.movie_details_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.usecase.TheMovieDatabaseApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor (
    private val theMovieDatabaseApiUseCase: TheMovieDatabaseApiUseCase,
) : ViewModel() {

    fun getMovieDetails(movieId: String) = viewModelScope.launch {
        val userDetails = theMovieDatabaseApiUseCase.getMovieDetails(movieId)
    }

}