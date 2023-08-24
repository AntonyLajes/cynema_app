package com.nomargin.cynema_app.ui.fragment.sign_up_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema_app.data.remote.entity.SignUpModel
import com.nomargin.cynema_app.data.repository.AuthenticationRepository
import com.nomargin.cynema_app.util.StatusModel
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
        if(result.statusModel != null){
            _attributesStatus.value =
                StatusModel(
                    result.statusModel.isValid,
                    result.statusModel.errorType,
                    result.statusModel.message
                )
        }
    }

}