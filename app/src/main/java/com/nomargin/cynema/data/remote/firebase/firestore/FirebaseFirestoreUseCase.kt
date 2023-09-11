package com.nomargin.cynema.data.remote.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore

interface FirebaseFirestoreUseCase {
    fun getFirebaseFirestore(): FirebaseFirestore
}