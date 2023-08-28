package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.remote.entity.SignUpModel
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.StatusModel

interface AuthenticationRepository {

    suspend fun signUp(signUpModel: SignUpModel): Resource<StatusModel>

}