package com.nomargin.cynema.data.remote.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseFirestoreUseCaseImpl @Inject constructor(): FirebaseFirestoreUseCase {
    override fun getFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }
}