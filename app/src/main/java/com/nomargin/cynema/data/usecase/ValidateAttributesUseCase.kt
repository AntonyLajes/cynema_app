package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.remote.entity.SignInModel
import com.nomargin.cynema.data.remote.entity.SignUpModel
import com.nomargin.cynema.util.model.StatusModel

interface ValidateAttributesUseCase {

    fun validateSignUpAttributes(signUpModel: SignUpModel): StatusModel
    fun validateSignInAttributes(signInModel: SignInModel): StatusModel
    fun validateSendPasswordResetEmailAttributes(email: String): StatusModel

}