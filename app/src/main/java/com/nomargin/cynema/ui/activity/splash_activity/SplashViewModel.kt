package com.nomargin.cynema.ui.activity.splash_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.repository.AuthenticationRepository
import com.nomargin.cynema.data.repository.ProfileRepository
import com.nomargin.cynema.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val profileRepository: ProfileRepository
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
        _isProfileCreated.value = profileRepository.verifyProfile()
    }

}