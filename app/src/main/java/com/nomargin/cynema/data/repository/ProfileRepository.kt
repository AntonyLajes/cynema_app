package com.nomargin.cynema.data.repository

import android.net.Uri
import com.nomargin.cynema.data.remote.firebase.entity.UserProfileDataModel
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel
import com.nomargin.cynema.util.model.UserProfileModel

interface ProfileRepository {

    suspend fun createProfile(userProfileModel: UserProfileModel): Resource<StatusModel>

    suspend fun uploadProfilePicture(imageUri: Uri): Uri?

    suspend fun verifyProfile(): Boolean

    suspend fun checkUserUsername(username: String): StatusModel?

    suspend fun getUserData(userId: String): Resource<UserProfileDataModel>

    suspend fun handlerFavoriteMovies(movieId: String): Resource<StatusModel>
}