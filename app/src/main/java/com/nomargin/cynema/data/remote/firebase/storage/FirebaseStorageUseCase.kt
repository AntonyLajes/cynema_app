package com.nomargin.cynema.data.remote.firebase.storage

import com.google.firebase.storage.FirebaseStorage

interface FirebaseStorageUseCase {
    fun getFirebaseStorage(): FirebaseStorage

}