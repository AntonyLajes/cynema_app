package com.nomargin.cynema_app.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.nomargin.cynema_app.data.remote.entity.SignUpModel
import com.nomargin.cynema_app.util.Resource
import com.nomargin.cynema_app.util.StatusModel

interface AuthenticationRepository {

    suspend fun signUp(signUpModel: SignUpModel): Resource<StatusModel>

}