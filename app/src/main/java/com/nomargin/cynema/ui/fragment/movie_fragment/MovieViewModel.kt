package com.nomargin.cynema.ui.fragment.movie_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.usecase.TheMovieDatabaseApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor (
    private val theMovieDatabaseApiUseCase: TheMovieDatabaseApiUseCase
) : ViewModel() {

    private fun getMovieDetails(movieId: String) = viewModelScope.launch {
        val movieDetails = theMovieDatabaseApiUseCase.getMovieDetails(movieId)
    }
}