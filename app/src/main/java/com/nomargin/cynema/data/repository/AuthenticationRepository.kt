package com.nomargin.cynema.data.repository

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.FirebaseUser
import com.nomargin.cynema.data.remote.entity.SignInModel
import com.nomargin.cynema.data.remote.entity.SignUpModel
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel


interface AuthenticationRepository {
    suspend fun verifyLogin(): Resource<FirebaseUser?>
    suspend fun sendPasswordResetEmail(email: String): Resource<StatusModel>
    suspend fun signUp(signUpModel: SignUpModel): Resource<StatusModel>
    suspend fun signIn(signInModel: SignInModel): Resource<StatusModel>
    suspend fun authWithCredential(credential: SignInCredential): Resource<StatusModel>
    suspend fun getAuthenticationRequest(): BeginSignInRequest
}