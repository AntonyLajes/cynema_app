package com.nomargin.cynema_app.ui.fragment.sign_up_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema_app.R
import com.nomargin.cynema_app.data.remote.entity.SignUpModel
import com.nomargin.cynema_app.data.repository.AuthenticationRepository
import com.nomargin.cynema_app.util.Constants
import com.nomargin.cynema_app.util.StatusModel
import com.nomargin.cynema_app.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private var _attributesStatus: MutableLiveData<StatusModel> = MutableLiveData()
    val attributesStatus: LiveData<StatusModel> = _attributesStatus

    fun signUp(signUpModel: SignUpModel) = viewModelScope.launch {
        val result = authenticationRepository.signUp(signUpModel)
        when(result.status){
            Status.SUCCESS -> {

            }
            else -> {

            }
        }
    }

}