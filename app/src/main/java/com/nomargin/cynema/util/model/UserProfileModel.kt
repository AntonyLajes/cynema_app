package com.nomargin.cynema.util.model

import android.net.Uri

data class UserProfileModel(
    var userProfileUri: Uri? = null,
    var userFirstName: String = "",
    var userLastName: String = "",
    var userUsername: String = "",
    var userBiography: String = ""
)