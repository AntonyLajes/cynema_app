package com.nomargin.cynema_app.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.nomargin.cynema_app.data.remote.entity.SignUpModel
import com.nomargin.cynema_app.data.remote.firebase.signin.FirebaseAuthUseCase
import com.nomargin.cynema_app.data.usecase.ValidateAttributesUseCase
import com.nomargin.cynema_app.util.Resource
import com.nomargin.cynema_app.util.StatusModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
                firebaseAuth.getFirebaseAuth()
                    .createUserWithEmailAndPassword(signUpModel.email, signUpModel.password)
                    .addOnCompleteListener { task ->
                        resultTask = if(task.isSuccessful){
                            Resource.success(null, validatedAttributes)
                        }else{
                            Resource.error("Error", null, validatedAttributes)
                        }
                    }.await()
                resultTask
        }else{
            Resource.error(msg = "Error", data = null, statusModel = validatedAttributes)
        }
    }

}