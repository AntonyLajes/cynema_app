package com.nomargin.cynema_app.data.remote.firebase.signin

import com.google.firebase.auth.FirebaseAuth

interface FirebaseAuthUseCase {

    fun getFirebaseAuth(): FirebaseAuth

}