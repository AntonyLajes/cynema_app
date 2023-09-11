package com.nomargin.cynema.data.remote.firebase.storage

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel

interface FirebaseStorageUseCase {
    fun getFirebaseStorage(): FirebaseStorage

}