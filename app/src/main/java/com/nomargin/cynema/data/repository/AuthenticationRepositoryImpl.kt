package com.nomargin.cynema.data.repository

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.entity.SignUpModel
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.remote.google.AuthenticationRequestUseCase
import com.nomargin.cynema.data.remote.google.AuthenticationRequestUseCaseImpl
import com.nomargin.cynema.data.usecase.ValidateAttributesUseCase
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.StatusModel
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuthUseCase,
    private val validateAttributes: ValidateAttributesUseCase,
    private val authenticationRequestUseCaseImpl: AuthenticationRequestUseCase
) : AuthenticationRepository {

    private lateinit var resultTask: Resource<StatusModel>

    override suspend fun signUp(signUpModel: SignUpModel): Resource<StatusModel> {
        val validatedAttributes = validateAttributes.validateAttributes(signUpModel)
        return if (validatedAttributes.isValid) {
            try {
                firebaseAuth.getFirebaseAuth()
                    .createUserWithEmailAndPassword(signUpModel.email, signUpModel.password)
                    .addOnCompleteListener { task ->
                        resultTask = if (task.isSuccessful) {
                            Resource.success(null, validatedAttributes)
                        } else {
                            Resource.error(
                                task.exception?.message ?: "Error",
                                null,
                                StatusModel(
                                    false,
                                    Constants.ERROR_TYPES.firebaseSignUpError,
                                    R.string.unknown_error
                                )
                            )
                        }
                    }.await()
                resultTask
            } catch (e: Exception) {
                Resource.error(
                    e.message ?: "Error",
                    null,
                    StatusModel(
                        false,
                        Constants.ERROR_TYPES.firebaseSignUpError,
                        R.string.unknown_error
                    )
                )
            }
        } else {
            Resource.error(msg = "Error", data = null, statusModel = validatedAttributes)
        }
    }

    override suspend fun authWithCredential(credential: SignInCredential): Resource<StatusModel> {
        val idToken = credential.googleIdToken
        return when {
            idToken != null -> {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.getFirebaseAuth().signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        resultTask = if (task.isSuccessful) {
                            Resource.success(
                                null,
                                StatusModel(
                                    true,
                                    null,
                                    R.string.auth_with_successfully
                                )
                            )
                        } else {
                            Log.w("firebaseAuth", "signInWithCredential:failure", task.exception)
                            Resource.error(
                                task.exception?.message.toString(),
                                null,
                                StatusModel(
                                    false,
                                    Constants.ERROR_TYPES.firebaseCredentialAuthError,
                                    R.string.auth_failed
                                )
                            )
                        }
                    }.await()
                resultTask
            }

            else -> {
                // Shouldn't happen.
                Resource.error(
                    "No ID token!",
                    null,
                    StatusModel(
                        false,
                        Constants.ERROR_TYPES.firebaseCredentialAuthError,
                        R.string.no_id_token
                    )
                )
            }
        }
    }

    override suspend fun getAuthenticationRequest(): BeginSignInRequest {
        return authenticationRequestUseCaseImpl.getAuthenticationRequest()
    }
}