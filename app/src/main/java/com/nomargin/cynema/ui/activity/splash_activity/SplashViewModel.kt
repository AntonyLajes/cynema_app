package com.nomargin.cynema.ui.activity.splash_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.repository.AuthenticationRepository
import com.nomargin.cynema.util.Status
import com.nomargin.cynema.util.model.StatusModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private var _currentUser: MutableLiveData<Status> = MutableLiveData()
    val currentUser: LiveData<Status> = _currentUser

    fun verifyCurrentUser() = viewModelScope.launch {
        val currentUser = authenticationRepository.verifyLogin()
        _currentUser.value = currentUser.status
    }

}