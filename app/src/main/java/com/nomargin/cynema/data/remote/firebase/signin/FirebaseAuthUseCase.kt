package com.nomargin.cynema.data.remote.firebase.signin

import com.google.firebase.auth.FirebaseAuth

interface FirebaseAuthUseCase {

    fun getFirebaseAuth(): FirebaseAuth

}