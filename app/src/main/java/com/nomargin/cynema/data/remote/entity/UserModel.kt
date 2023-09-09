package com.nomargin.cynema.data.remote.entity

data class UserModel(
    val userId: String,
    val userUsername: String,
    val userEmail: String,
    val userFirstName: String,
    val userLastName: String,
    val userPosts: List<Int>,
    val userPostsQuantity: Int,
    val userPictureProfile: String,
    val userBiography: String,
    val isProfileCreated: Boolean
)