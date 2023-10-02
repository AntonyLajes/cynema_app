package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.local.entity.PostModel
import com.nomargin.cynema.util.model.SignInModel
import com.nomargin.cynema.util.model.SignUpModel
import com.nomargin.cynema.util.model.StatusModel
import com.nomargin.cynema.util.model.UserProfileModel

interface ValidateAttributesUseCase {

    fun validateSignUpAttributes(signUpModel: SignUpModel): StatusModel
    fun validateSignInAttributes(signInModel: SignInModel): StatusModel
    fun validateSendPasswordResetEmailAttributes(email: String): StatusModel
    fun validateUserProfile(userProfileModel: UserProfileModel): StatusModel
    fun validatePost(postModel: PostModel): StatusModel
}