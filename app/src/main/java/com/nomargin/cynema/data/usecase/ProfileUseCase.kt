package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.remote.firebase.entity.UserProfileDataModel

interface ProfileUseCase {

    suspend fun getUserData(): UserProfileDataModel?

}