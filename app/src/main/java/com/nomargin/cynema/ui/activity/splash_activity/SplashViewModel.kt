package com.nomargin.cynema.ui.activity.splash_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.repository.AuthenticationRepository
import com.nomargin.cynema.data.repository.LocalDataRepository
import com.nomargin.cynema.data.repository.ProfileRepository
import com.nomargin.cynema.data.repository.TheMovieDatabaseRepository
import com.nomargin.cynema.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val profileRepository: ProfileRepository,
    private val theMovieDatabaseRepository: TheMovieDatabaseRepository,
    private val localDataRepository: LocalDataRepository
) : ViewModel() {

    private var _currentUser: MutableLiveData<Status> = MutableLiveData()
    val currentUser: LiveData<Status> = _currentUser
    private var _isProfileCreated: MutableLiveData<Boolean> = MutableLiveData()
    val isProfileCreated: LiveData<Boolean> = _isProfileCreated

    fun verifyCurrentUser() = viewModelScope.launch {
        val currentUser = authenticationRepository.verifyLogin()
        _currentUser.value = currentUser.status
    }

    fun verifyIfProfileIsCreated() = viewModelScope.launch {
        getGenres().await()
        _isProfileCreated.value = profileRepository.verifyProfile()
    }

    private fun getGenres() = viewModelScope.async{
        val genres = theMovieDatabaseRepository.getMovieGenres()
        localDataRepository.insert(genres.genreList)
    }

}