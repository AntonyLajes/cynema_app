package com.nomargin.cynema.data.repository

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInCredential
import com.nomargin.cynema.data.remote.entity.SignUpModel
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.StatusModel


interface AuthenticationRepository {

    suspend fun signUp(signUpModel: SignUpModel): Resource<StatusModel>
    suspend fun authWithCredential(credential: SignInCredential): Resource<StatusModel>
    suspend fun getAuthenticationRequest(): BeginSignInRequest
}