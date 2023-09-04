package com.nomargin.cynema.data.remote.google

import com.google.android.gms.auth.api.identity.BeginSignInRequest

interface AuthenticationRequestUseCase {

    suspend fun getAuthenticationRequest(): BeginSignInRequest

}
