package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.usecase.ValidateAttributesUseCase
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel
import com.nomargin.cynema.util.model.UserProfileModel
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuthUseCase,
    private val validateAttributes: ValidateAttributesUseCase,
): ProfileRepository {

    private lateinit var createProfileResult: Resource<StatusModel>

    override suspend fun createProfile(userProfileModel: UserProfileModel): Resource<StatusModel> {
        val validateCreateProfileAttributes = validateAttributes.validateUserProfile(userProfileModel)
        if(validateCreateProfileAttributes.isValid){
            
        }else{
            createProfileResult = Resource.error(
                "Error",
                null,
                validateCreateProfileAttributes
            )
        }
        return createProfileResult
    }
}