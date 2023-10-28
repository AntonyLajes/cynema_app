package com.nomargin.cynema.data.repository

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObject
import com.nomargin.cynema.BuildConfig
import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.remote.firebase.entity.UserProfileDataModel
import com.nomargin.cynema.data.remote.firebase.firestore.FirebaseFirestoreUseCase
import com.nomargin.cynema.data.remote.firebase.storage.FirebaseStorageUseCase
import com.nomargin.cynema.data.usecase.ValidateAttributesUseCase
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel
import com.nomargin.cynema.util.model.UserProfileModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class ProfileRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorageUseCase,
    private val firebaseFirestore: FirebaseFirestoreUseCase,
    private val firebaseAuth: FirebaseAuthUseCase,
    private val validateAttributes: ValidateAttributesUseCase,
) : ProfileRepository {

    private lateinit var createProfileResult: Resource<StatusModel>
    private val database = firebaseFirestore.getFirebaseFirestore()
        .collection("${Constants.FIRESTORE.rootCollection}/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}/${Constants.FIRESTORE.usersCollection}")
        .document(firebaseAuth.getFirebaseAuth().currentUser?.uid.toString())

    override suspend fun createProfile(userProfileModel: UserProfileModel): Resource<StatusModel> {
        val validateCreateProfileAttributes =
            validateAttributes.validateUserProfile(userProfileModel)
        val userData = getUserData(
            firebaseAuth.getFirebaseAuth().currentUser?.uid.toString()
        )
        if (validateCreateProfileAttributes.isValid) {
            val profilePictureLink = when {
                userProfileModel.userProfileUri != null -> {
                    uploadProfilePicture(userProfileModel.userProfileUri!!)
                }

                userData.data?.profilePicture?.isNotEmpty() == true -> {
                    userData.data.profilePicture
                }

                else -> {
                    ""
                }
            }
            val user = UserProfileDataModel(
                id = firebaseAuth.getFirebaseAuth().currentUser!!.uid,
                username = userProfileModel.userUsername,
                email = firebaseAuth.getFirebaseAuth().currentUser!!.email,
                firstName = userProfileModel.userFirstName,
                lastName = userProfileModel.userLastName,
                biography = userProfileModel.userBiography,
                profilePicture = profilePictureLink.toString(),
                isProfileUpdated = true,
                posts = if (userData.data?.posts?.isEmpty() == true){
                    emptyList()
                }else{
                    userData.data?.posts
                }
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
            storageRef.child("${BuildConfig.FIREBASE_FLAVOR_COLLECTION}${Constants.STORAGE.profilePictureRoot}${imageUri.lastPathSegment}")
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
        return database.get().await()
            .exists()
    }

    override suspend fun checkUserUsername(username: String): StatusModel? {

        if (username.length >= Constants.MIN_LENGTH.usernameMinLength && username.length <= Constants.MAX_LENGTH.userUsernameMaxLength) {
            val usernameDoNotExists = firebaseFirestore.getFirebaseFirestore()
                .collection("${Constants.FIRESTORE.rootCollection}/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}/${Constants.FIRESTORE.usersCollection}")
                .whereEqualTo("user_username", username)
                .get()
                .addOnCompleteListener { }
                .await().isEmpty

            return if (usernameDoNotExists) {
                StatusModel(
                    true,
                    null,
                    R.string.user_username_is_valid
                )
            } else {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.userUsernameAlreadyInUse,
                    R.string.user_username_already_in_use
                )
            }
        } else {
            return null
        }
    }

    override suspend fun getUserData(userId: String): Resource<UserProfileDataModel> {
        val user = firebaseFirestore
            .getFirebaseFirestore()
            .collection(
                "${Constants.FIRESTORE.rootCollection}/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}/${Constants.FIRESTORE.usersCollection}"
            ).document(
                userId
            ).get()
            .addOnCompleteListener { }.await()

        return if (user.exists()) {
            Resource.success(
                user.toObject<UserProfileDataModel>(),
                StatusModel(
                    true,
                    null,
                    R.string.user_data_reached_with_success
                )
            )
        } else {
            Resource.error(
                "Error",
                null,
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.couldNotReachTheUserData,
                    R.string.could_not_reach_the_user_data
                )
            )
        }
    }

    override suspend fun updateProfileWhenUserCreateOrDeleteAPoster(
        updateType: Constants.UPDATE_TYPE,
        postId: String,
    ): StatusModel? {
        return suspendCoroutine { continuation ->
            when (updateType) {
                Constants.UPDATE_TYPE.AddPost -> {
                    database.update(
                        "posts",
                        FieldValue.arrayUnion(postId)
                    ).addOnSuccessListener {
                        continuation.resumeWith(
                            Result.success(
                                StatusModel(
                                    true,
                                    null,
                                    R.string.post_created_with_successfully
                                )
                            )
                        )
                    }.addOnFailureListener {
                        continuation.resumeWith(Result.failure(it))
                    }
                }

                else -> {
                    database.update(
                        "posts",
                        FieldValue.arrayRemove(postId)
                    ).addOnSuccessListener {
                        continuation.resumeWith(
                            Result.success(
                                StatusModel(
                                    true,
                                    null,
                                    R.string.post_deleted_with_success
                                )
                            )
                        )
                    }.addOnFailureListener {
                        continuation.resumeWith(Result.failure(it))
                    }
                }
            }
        }
    }

    override suspend fun handlerFavoriteMovies(
        movieId: String,
    ): Resource<StatusModel> {
        val userData = getUserData(firebaseAuth.getFirebaseAuth().currentUser?.uid.toString())
        return suspendCoroutine { continuation ->
            if (userData.statusModel?.isValid == true) {
                if (userData.data?.favoriteMovies?.contains(movieId) == true) {
                    database.update(
                        "favoriteMovies", FieldValue.arrayRemove(movieId)
                    ).addOnSuccessListener {
                        continuation.resumeWith(
                            Result.success(
                                Resource.success(
                                    null,
                                    StatusModel(
                                        true,
                                        null,
                                        R.string.movie_removed_from_your_favorites
                                    )
                                )
                            )
                        )
                    }.addOnFailureListener {
                        continuation.resumeWith(Result.failure(it))
                    }
                } else {
                    database.update(
                        "favoriteMovies", FieldValue.arrayUnion(movieId)
                    ).addOnSuccessListener {
                        continuation.resumeWith(
                            Result.success(
                                Resource.success(
                                    null,
                                    StatusModel(
                                        true,
                                        null,
                                        R.string.movie_added_to_your_favorites
                                    )
                                )
                            )
                        )
                    }.addOnFailureListener {
                        continuation.resumeWith(Result.failure(it))
                    }
                }
            } else {
                continuation.resumeWith(
                    Result.success(
                        Resource.error(
                            "Error",
                            null,
                            StatusModel(
                                false,
                                null,
                                R.string.error
                            )
                        )
                    )
                )
            }
        }
    }
}