package com.nomargin.cynema.ui.activity.forgot_password_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.repository.AuthenticationRepository
import com.nomargin.cynema.util.model.StatusModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {

    private var _sendPasswordResetEmailStatus: MutableLiveData<StatusModel?> = MutableLiveData()
    val sendPasswordResetEmailStatus: LiveData<StatusModel?> = _sendPasswordResetEmailStatus

    fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
        _sendPasswordResetEmailStatus.value = authenticationRepository.sendPasswordResetEmail(email).statusModel
    }

}