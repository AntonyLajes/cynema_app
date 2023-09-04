package com.nomargin.cynema.ui.fragment.sign_in_fragment

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
import com.nomargin.cynema.data.remote.entity.SignInModel
import com.nomargin.cynema.data.repository.AuthenticationRepository
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.StatusModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private var _oneTapStatus: MutableLiveData<StatusModel> = MutableLiveData()
    val oneTapStatus: LiveData<StatusModel> = _oneTapStatus
    private var _attributesStatus: MutableLiveData<StatusModel?> = MutableLiveData()
    val attributesStatus: LiveData<StatusModel?> = _attributesStatus

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

    fun signIn(signInModel: SignInModel) = viewModelScope.launch {
        val result = authenticationRepository.signIn(signInModel)
            _attributesStatus.value = result.statusModel
    }
}