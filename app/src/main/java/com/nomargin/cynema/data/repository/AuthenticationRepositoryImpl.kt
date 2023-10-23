package com.nomargin.cynema.data.repository

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.remote.google.AuthenticationRequestUseCase
import com.nomargin.cynema.data.usecase.ValidateAttributesUseCase
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.SignInModel
import com.nomargin.cynema.util.model.SignUpModel
import com.nomargin.cynema.util.model.StatusModel
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class AuthenticationRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuthUseCase,
    private val validateAttributes: ValidateAttributesUseCase,
    private val authenticationRequestUseCaseImpl: AuthenticationRequestUseCase,
) : AuthenticationRepository {

    override suspend fun verifyLogin(): Resource<FirebaseUser?> {
        val currentUser = firebaseAuth.getFirebaseAuth().currentUser
        return if (currentUser != null) {
            Resource.success(
                currentUser,
                StatusModel(
                    true,
                    null,
                    R.string.auth_with_successfully
                )
            )
        } else {
            Resource.error(
                "User is not logged in",
                null,
                StatusModel(
                    false,
                    null,
                    R.string.user_is_not_logged_in
                )
            )
        }
    }

    override suspend fun signUp(signUpModel: SignUpModel): Resource<StatusModel> {
        return suspendCoroutine { continuation ->
            val validatedSignUpAttributes = validateAttributes.validateSignUpAttributes(signUpModel)
            if (validatedSignUpAttributes.isValid) {
                try {
                    firebaseAuth.getFirebaseAuth().createUserWithEmailAndPassword(
                        signUpModel.email,
                        signUpModel.password
                    ).addOnSuccessListener {
                        continuation.resumeWith(
                            Result.success(
                                Resource.success(
                                    null,
                                    StatusModel(
                                        true,
                                        null,
                                        R.string.sign_up_with_successfully
                                    )
                                )
                            )
                        )
                    }.addOnFailureListener {
                        continuation.resumeWith(Result.failure(it))
                    }
                } catch (e: Exception) {
                    continuation.resumeWith(Result.failure(e))
                }
            } else {
                continuation.resumeWith(
                    Result.success(
                        Resource.error(
                            msg = "Error",
                            data = null,
                            statusModel = validatedSignUpAttributes
                        )
                    )
                )
            }
        }
    }

    override suspend fun signIn(signInModel: SignInModel): Resource<StatusModel> {
        return suspendCoroutine { continuation ->
            val validatedSignInAttributes = validateAttributes.validateSignInAttributes(signInModel)
            if (validatedSignInAttributes.isValid) {
                try {
                    firebaseAuth.getFirebaseAuth().signInWithEmailAndPassword(
                        signInModel.email,
                        signInModel.password
                    ).addOnSuccessListener {
                        continuation.resumeWith(
                            Result.success(
                                Resource.success(
                                    null,
                                    StatusModel(
                                        true,
                                        null,
                                        R.string.auth_with_successfully
                                    )
                                )
                            )
                        )
                    }.addOnFailureListener {
                        continuation.resumeWith(Result.failure(it))
                    }
                } catch (e: Exception) {
                    continuation.resumeWith(Result.failure(e))
                }
            } else {
                continuation.resumeWith(
                    Result.success(
                        Resource.error(
                            msg = "Error",
                            data = null,
                            statusModel = validatedSignInAttributes
                        )
                    )
                )
            }
        }
    }

    override suspend fun authWithCredential(credential: SignInCredential): Resource<StatusModel> {
        return suspendCoroutine { continuation ->
            val idToken = credential.googleIdToken
            when {
                idToken != null -> {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    firebaseAuth.getFirebaseAuth().signInWithCredential(firebaseCredential)
                        .addOnSuccessListener {
                            continuation.resumeWith(
                                Result.success(
                                    Resource.success(
                                        null,
                                        StatusModel(
                                            true,
                                            null,
                                            R.string.auth_with_successfully
                                        )
                                    )
                                )
                            )
                        }
                        .addOnFailureListener {
                            continuation.resumeWith(Result.failure(it))
                        }
                }

                else -> {
                    continuation.resumeWith(
                        Result.success(
                            Resource.error(
                                "No ID token!",
                                null,
                                StatusModel(
                                    false,
                                    Constants.ERROR_TYPES.firebaseCredentialAuthError,
                                    R.string.no_id_token
                                )
                            )
                        )
                    )
                }
            }
        }
    }

    override suspend fun getAuthenticationRequest(): BeginSignInRequest {
        return authenticationRequestUseCaseImpl.getAuthenticationRequest()
    }

    private fun firebaseOnFailure(exception: Exception): Resource<StatusModel> {
        return if (exception == FirebaseAuthException::class.java) {
            val errorCode = (exception as FirebaseAuthException).errorCode
            val errorMessage = Constants.AUTH_ERRORS.authErrors[errorCode] ?: R.string.unknown_error
            Resource.error(
                exception.message ?: "Error",
                null,
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.firebaseAuthError,
                    errorMessage
                )
            )
        } else {
            Resource.error(
                exception.message ?: "Error",
                null,
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.firebaseAuthError,
                    R.string.unknown_error
                )
            )
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Resource<StatusModel> {
        return suspendCoroutine { continuation ->
            val validateSendPasswordResetEmailCredential =
                validateAttributes.validateSendPasswordResetEmailAttributes(email)
            if (validateSendPasswordResetEmailCredential.isValid) {
                try {
                    firebaseAuth.getFirebaseAuth().sendPasswordResetEmail(
                        email
                    ).addOnSuccessListener {
                        continuation.resumeWith(
                            Result.success(
                                Resource.success(
                                    null,
                                    StatusModel(
                                        true,
                                        null,
                                        R.string.sent_password_reset_email_with_success
                                    )
                                )
                            )
                        )
                    }.addOnFailureListener {
                        continuation.resumeWith(Result.failure(it))
                    }
                } catch (e: Exception) {
                    continuation.resumeWith(Result.failure(e))
                }
            } else {
                continuation.resumeWith(
                    Result.success(
                        Resource.error(
                            msg = "Error",
                            data = null,
                            statusModel = validateSendPasswordResetEmailCredential
                        )
                    )
                )
            }
        }
    }

    private fun catchError(exception: Exception): Resource<StatusModel> {
        val errorCode = (exception as FirebaseAuthException).errorCode
        val errorMessage = Constants.AUTH_ERRORS.authErrors[errorCode] ?: R.string.unknown_error
        return Resource.error(
            exception.message ?: "Error",
            null,
            StatusModel(
                false,
                Constants.ERROR_TYPES.firebaseAuthError,
                errorMessage
            )
        )
    }
}