package com.nomargin.cynema.data.repository

import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.nomargin.cynema.R
import com.nomargin.cynema.util.model.SignInModel
import com.nomargin.cynema.util.model.SignUpModel
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.remote.google.AuthenticationRequestUseCase
import com.nomargin.cynema.data.usecase.ValidateAttributesUseCase
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuthUseCase,
    private val validateAttributes: ValidateAttributesUseCase,
    private val authenticationRequestUseCaseImpl: AuthenticationRequestUseCase
) : AuthenticationRepository {

    private lateinit var resultSignUpTask: Resource<StatusModel>
    private lateinit var resultSignInTask: Resource<StatusModel>
    private lateinit var resultSendPasswordResetEmailTask: Resource<StatusModel>

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
        val validatedSignUpAttributes = validateAttributes.validateSignUpAttributes(signUpModel)
        if (validatedSignUpAttributes.isValid) {
            try {
                firebaseAuth.getFirebaseAuth().createUserWithEmailAndPassword(
                    signUpModel.email,
                    signUpModel.password
                ).addOnSuccessListener {
                    resultSignUpTask = Resource.success(
                        null,
                        StatusModel(
                            true,
                            null,
                            R.string.sign_up_with_successfully
                        )
                    )
                }.addOnFailureListener {
                    resultSignUpTask = firebaseOnFailure(it)
                }.await()
            } catch (e: Exception) {
                resultSignUpTask = catchError(e)
            }
        } else {
            resultSignUpTask =
                Resource.error(
                    msg = "Error",
                    data = null,
                    statusModel = validatedSignUpAttributes
                )
        }
        return resultSignUpTask
    }

    override suspend fun signIn(signInModel: SignInModel): Resource<StatusModel> {
        val validatedSignInAttributes = validateAttributes.validateSignInAttributes(signInModel)
        if (validatedSignInAttributes.isValid) {
            try {
                firebaseAuth.getFirebaseAuth().signInWithEmailAndPassword(
                    signInModel.email,
                    signInModel.password
                ).addOnSuccessListener {
                    resultSignInTask = Resource.success(
                        null,
                        StatusModel(
                            true,
                            null,
                            R.string.auth_with_successfully
                        )
                    )
                }.addOnFailureListener {
                    resultSignInTask = firebaseOnFailure(it)
                }.await()
            } catch (e: Exception) {
                resultSignInTask = catchError(e)
            }
        } else {
            resultSignInTask =
                Resource.error(msg = "Error", data = null, statusModel = validatedSignInAttributes)
        }
        return resultSignInTask
    }

    override suspend fun authWithCredential(credential: SignInCredential): Resource<StatusModel> {
        val idToken = credential.googleIdToken
        return when {
            idToken != null -> {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.getFirebaseAuth().signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        resultSignUpTask = if (task.isSuccessful) {
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
                resultSignUpTask
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
        val validateSendPasswordResetEmailCredential =
            validateAttributes.validateSendPasswordResetEmailAttributes(email)
        if (validateSendPasswordResetEmailCredential.isValid) {
            try {
                firebaseAuth.getFirebaseAuth().sendPasswordResetEmail(
                    email
                ).addOnSuccessListener {
                    resultSendPasswordResetEmailTask = Resource.success(
                        null,
                        StatusModel(
                            true,
                            null,
                            R.string.sent_password_reset_email_with_success
                        )
                    )
                }.addOnFailureListener {
                    resultSendPasswordResetEmailTask = firebaseOnFailure(it)
                }.await()
            } catch (e: Exception) {
                resultSendPasswordResetEmailTask = catchError(e)
            }
        } else {
            resultSendPasswordResetEmailTask = Resource.error(msg = "Error", data = null, statusModel = validateSendPasswordResetEmailCredential)
        }
        return resultSendPasswordResetEmailTask
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