package com.nomargin.cynema.data.remote.firebase.entity

data class UserProfileDataModel(
    val id: String? = null,
    val username: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val biography: String? = null,
    val profilePicture: String? = null,
    val posts: List<String>? = null,
    val postsQuantity: Int? = null,
    val votedPosts: List<String?> = listOf(),
    @field:JvmField
    val isProfileUpdated: Boolean? = null,
)

/*
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
 */
