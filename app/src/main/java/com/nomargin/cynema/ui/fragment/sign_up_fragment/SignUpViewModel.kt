package com.nomargin.cynema.ui.fragment.sign_up_fragment

import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.entity.SignUpModel
import com.nomargin.cynema.data.repository.AuthenticationRepository
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.model.StatusModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private var _attributesStatus: MutableLiveData<StatusModel?> = MutableLiveData()
    val attributesStatus: LiveData<StatusModel?> = _attributesStatus
    private var _oneTapStatus: MutableLiveData<StatusModel> = MutableLiveData()
    val oneTapStatus: LiveData<StatusModel> = _oneTapStatus
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

    suspend fun beginAuthenticationRequest(): BeginSignInRequest {
        return authenticationRepository.getAuthenticationRequest()
    }

    fun verifyIdToken(
        result: ActivityResult,
        oneTapClient: SignInClient,
    ) {
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            viewModelScope.launch {
                _oneTapStatus.value =
                    authenticationRepository.authWithCredential(credential).statusModel!!
            }
        } catch (e: ApiException) {
            when (e.statusCode) {
                CommonStatusCodes.CANCELED -> {
                    _oneTapStatus.value =
                        StatusModel(
                            false,
                            Constants.ERROR_TYPES.apiException,
                            R.string.one_tap_dialog_was_closed
                        )
                }

                CommonStatusCodes.NETWORK_ERROR -> {
                    _oneTapStatus.value =
                        StatusModel(
                            false,
                            Constants.ERROR_TYPES.apiException,
                            R.string.one_tap_encountered_a_network_error
                        )
                }

                else -> {
                    _oneTapStatus.value =
                        StatusModel(
                            false,
                            Constants.ERROR_TYPES.apiException,
                            R.string.couldn_t_get_credential_from_result
                        )
                }
            }
        }
    }

}