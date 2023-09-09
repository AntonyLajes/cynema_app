package com.nomargin.cynema.data.repository

import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel
import com.nomargin.cynema.util.model.UserProfileModel

interface ProfileRepository {

    suspend fun createProfile(userProfileModel: UserProfileModel): Resource<StatusModel>

}