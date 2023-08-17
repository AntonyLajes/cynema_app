package com.nomargin.cynema_app.data.repository

import androidx.annotation.StringRes
import com.nomargin.cynema_app.R
import com.nomargin.cynema_app.data.remote.entity.SignUpModel
import com.nomargin.cynema_app.util.Resource
import com.nomargin.cynema_app.util.StatusModel

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

}