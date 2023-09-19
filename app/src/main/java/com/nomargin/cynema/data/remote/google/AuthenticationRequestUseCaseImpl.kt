package com.nomargin.cynema.data.remote.google

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.nomargin.cynema.util.Private
import javax.inject.Inject

class AuthenticationRequestUseCaseImpl @Inject constructor(
): AuthenticationRequestUseCase {

    override suspend fun getAuthenticationRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(Private.webClientId)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }

}