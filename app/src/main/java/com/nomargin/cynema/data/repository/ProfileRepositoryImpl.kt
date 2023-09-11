package com.nomargin.cynema.data.repository

import android.net.Uri
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.remote.firebase.firestore.FirebaseFirestoreUseCase
import com.nomargin.cynema.data.remote.firebase.storage.FirebaseStorageUseCase
import com.nomargin.cynema.data.usecase.ValidateAttributesUseCase
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel
import com.nomargin.cynema.util.model.UserProfileModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorageUseCase,
    private val firebaseFirestore: FirebaseFirestoreUseCase,
    private val firebaseAuth: FirebaseAuthUseCase,
    private val validateAttributes: ValidateAttributesUseCase,
) : ProfileRepository {

    private lateinit var createProfileResult: Resource<StatusModel>

    override suspend fun createProfile(userProfileModel: UserProfileModel): Resource<StatusModel> {
        val validateCreateProfileAttributes =
            validateAttributes.validateUserProfile(userProfileModel)
        if (validateCreateProfileAttributes.isValid) {

            val imageUrl = uploadProfilePicture(userProfileModel.userProfileUri!!)

        } else {
            createProfileResult = Resource.error(
                "Error",
                null,
                validateCreateProfileAttributes
            )
        }
        return createProfileResult
    }

    override suspend fun uploadProfilePicture(imageUri: Uri): Uri {
        val storageRef = firebaseStorage.getFirebaseStorage().reference
        val profilePictureRef = storageRef.child("${Constants.STORAGE.profilePictureRoot}${imageUri.lastPathSegment}")
        val uploadProfilePicture = profilePictureRef.putFile(imageUri)
        return uploadProfilePicture.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            profilePictureRef.downloadUrl
        }.addOnCompleteListener {}.await()
    }
}