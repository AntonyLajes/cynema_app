package com.nomargin.cynema.data.remote.firebase.authentication

import com.google.firebase.auth.FirebaseAuth

interface FirebaseAuthUseCase {

    fun getFirebaseAuth(): FirebaseAuth

}