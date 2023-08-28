package com.nomargin.cynema.data.repository

import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.entity.SignUpModel
import com.nomargin.cynema.data.remote.firebase.signin.FirebaseAuthUseCase
import com.nomargin.cynema.data.usecase.ValidateAttributesUseCase
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.StatusModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuthUseCase,
    private val validateAttributes: ValidateAttributesUseCase
) : AuthenticationRepository {

    private lateinit var resultTask: Resource<StatusModel>

    override suspend fun signUp(signUpModel: SignUpModel): Resource<StatusModel> {
        val validatedAttributes = validateAttributes.validateAttributes(signUpModel)
        return if (validatedAttributes.isValid) {
                try{
                    firebaseAuth.getFirebaseAuth()
                        .createUserWithEmailAndPassword(signUpModel.email, signUpModel.password)
                        .addOnCompleteListener { task ->
                            resultTask = if(task.isSuccessful){
                                Resource.success(null, validatedAttributes)
                            }else{
                                Resource.error(task.exception?.message ?: "Error", null, StatusModel(false, Constants.ERROR_TYPES.firebaseSignUpError, R.string.unknown_error))
                            }
                        }.await()
                    resultTask
                }catch (e: Exception){
                    Resource.error(e.message ?: "Error", null, StatusModel(false, Constants.ERROR_TYPES.firebaseSignUpError, R.string.unknown_error))
                }
        }else{
            Resource.error(msg = "Error", data = null, statusModel = validatedAttributes)
        }
    }

}