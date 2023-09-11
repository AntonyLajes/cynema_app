package com.nomargin.cynema.util.model

import android.net.Uri

data class UserProfileModel(
    val userProfileUri: Uri?,
    val userFirstName: String,
    val userLastName: String,
    val userUsername: String,
    val userBiography: String
)