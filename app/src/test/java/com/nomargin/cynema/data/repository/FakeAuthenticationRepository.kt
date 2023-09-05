package com.nomargin.cynema.data.repository

import androidx.annotation.StringRes
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.FirebaseUser
import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.entity.SignInModel
import com.nomargin.cynema.data.remote.entity.SignUpModel
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel

class FakeAuthenticationRepository: AuthenticationRepository {

    private var shouldBeError: Boolean = false
    private var errorType: Int? = null
    private var message: Int = R.string.sign_up_with_successfully

    override suspend fun signUp(signUpModel: SignUpModel): Resource<StatusModel> {
        return if (shouldBeError) {
            Resource.error("Error", StatusModel(false, errorType, message), null)
        }else{
            Resource.success(StatusModel(false, errorType, message), null)
        }
    }

    fun shouldHappenAnError(
        value: Boolean,
        type: Int?,
        @StringRes messageRes: Int
    ){
        shouldBeError = value
        errorType = type
        message = messageRes
    }

    override suspend fun verifyLogin(): Resource<FirebaseUser?> {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(signInModel: SignInModel): Resource<StatusModel> {
        TODO("Not yet implemented")
    }

    override suspend fun authWithCredential(credential: SignInCredential): Resource<StatusModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getAuthenticationRequest(): BeginSignInRequest {
        TODO("Not yet implemented")
    }
}