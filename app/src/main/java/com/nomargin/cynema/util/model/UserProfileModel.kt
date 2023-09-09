package com.nomargin.cynema.util.model

import android.graphics.Bitmap

data class UserProfileModel(
    val userProfilePictureBitMap: Bitmap,
    val userFirstName: String,
    val userLastName: String,
    val userUsername: String,
    val userBiography: String
)