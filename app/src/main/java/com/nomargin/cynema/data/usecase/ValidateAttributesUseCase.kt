package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.remote.entity.SignUpModel
import com.nomargin.cynema.util.StatusModel

interface ValidateAttributesUseCase {

    fun validateAttributes(signUpModel: SignUpModel): StatusModel

}