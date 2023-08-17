package com.nomargin.cynema_app.data.usecase

import com.nomargin.cynema_app.data.remote.entity.SignUpModel
import com.nomargin.cynema_app.util.StatusModel

interface ValidateAttributesUseCase {

    fun validateAttributes(signUpModel: SignUpModel): StatusModel

}