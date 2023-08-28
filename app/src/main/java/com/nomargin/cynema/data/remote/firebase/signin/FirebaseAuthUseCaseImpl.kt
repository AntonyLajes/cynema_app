package com.nomargin.cynema.data.remote.firebase.signin

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseAuthUseCaseImpl @Inject constructor(): FirebaseAuthUseCase {
    override fun getFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }
}