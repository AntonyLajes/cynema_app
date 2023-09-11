package com.nomargin.cynema.data.remote.firebase.storage

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

class FirebaseStorageUseCaseImpl @Inject constructor(): FirebaseStorageUseCase {

    override fun getFirebaseStorage(): FirebaseStorage {
        return Firebase.storage
    }

}