package com.nomargin.cynema.data.repository

import android.net.Uri
import com.nomargin.cynema.R
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
    private val database = firebaseFirestore.getFirebaseFirestore()
        .collection(Constants.FIRESTORE.usersCollection)
        .document(firebaseAuth.getFirebaseAuth().currentUser?.uid.toString())

    override suspend fun createProfile(userProfileModel: UserProfileModel): Resource<StatusModel> {
        val validateCreateProfileAttributes =
            validateAttributes.validateUserProfile(userProfileModel)
        if (validateCreateProfileAttributes.isValid) {
            val profilePictureLink = userProfileModel.userProfileUri?.let {
                uploadProfilePicture(it)
            } ?: ""
            val user = hashMapOf(
                "user_id" to firebaseAuth.getFirebaseAuth().currentUser?.uid.toString(),
                "user_username" to userProfileModel.userUsername,
                "user_email" to firebaseAuth.getFirebaseAuth().currentUser!!.email,
                "user_first_name" to userProfileModel.userFirstName,
                "user_last_name" to userProfileModel.userLastName,
                "user_biography" to userProfileModel.userBiography,
                "user_profile_picture" to profilePictureLink,
                "user_posts" to arrayListOf<Int>(),
                "user_posts_quantity" to 0,
                "user_is_profile_updated" to true,
            )

            database.set(user)
                .addOnSuccessListener {
                    createProfileResult = Resource.success(
                        null,
                        StatusModel(
                            true,
                            null,
                            R.string.profile_updated_with_success
                        )
                    )
                }.addOnFailureListener {
                    createProfileResult = Resource.error(
                        "Error",
                        null,
                        StatusModel(
                            false,
                            null,
                            R.string.unknown_error
                        )
                    )
                }.await()
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
        val profilePictureRef =
            storageRef.child("${Constants.STORAGE.profilePictureRoot}${imageUri.lastPathSegment}")
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

    override suspend fun verifyProfile(): Boolean {
        return database.get()
            .addOnSuccessListener {}
            .addOnFailureListener {}.await().exists()
    }

    override suspend fun checkUserUsername(username: String): StatusModel? {
        if(username.length >= Constants.MIN_LENGTH.usernameMinLength && username.length <= Constants.MAX_LENGTH.userUsernameMaxLength){
            val usernameDoNotExists = firebaseFirestore.getFirebaseFirestore()
                .collection(Constants.FIRESTORE.usersCollection)
                .whereEqualTo("user_username", username)
                .get()
                .addOnCompleteListener {  }
                .await().isEmpty

            return if(usernameDoNotExists){
                StatusModel(
                    true,
                    null,
                    R.string.user_username_is_valid
                )
            }else{
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.userUsernameAlreadyInUse,
                    R.string.user_username_already_in_use
                )
            }
        }else{
            return null
        }
    }
}