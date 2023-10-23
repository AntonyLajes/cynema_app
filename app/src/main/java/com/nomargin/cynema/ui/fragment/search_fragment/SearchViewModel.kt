package com.nomargin.cynema.ui.fragment.search_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.local.entity.MovieSearchedAppearanceModel
import com.nomargin.cynema.data.repository.SharedPreferencesRepository
import com.nomargin.cynema.data.usecase.TheMovieDatabaseApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val theMovieDatabaseApiUseCase: TheMovieDatabaseApiUseCase,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : ViewModel() {

    private var _movieSearchByQueryResult: MutableLiveData<MovieSearchedAppearanceModel> =
        MutableLiveData()
    val movieSearchByQueryResult: LiveData<MovieSearchedAppearanceModel> = _movieSearchByQueryResult
    private var _popularMoviesResult: MutableLiveData<MovieSearchedAppearanceModel> =
        MutableLiveData()
    val popularMoviesResult: LiveData<MovieSearchedAppearanceModel> = _popularMoviesResult


    fun doSearch(query: String?) = viewModelScope.launch {
        query?.let {
            _movieSearchByQueryResult.value = theMovieDatabaseApiUseCase.getMovieDetailsByQuery(it)
        }
    }

    fun getPopularMovies() = viewModelScope.launch{
        _popularMoviesResult.value = theMovieDatabaseApiUseCase.getPopularMoviesToSearchFragment()
    }

    fun saveDataToSharedPreferences(key: String, movieId: String){
        sharedPreferencesRepository.putString(key, movieId)
    }
}