package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.remote.firebase.entity.UserProfileDataModel
import com.nomargin.cynema.data.repository.ProfileRepository
import com.nomargin.cynema.util.Status
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class ProfileUseCaseImpl @Inject constructor(
    private var profileRepository: ProfileRepository,
    private var firebaseAuthUseCase: FirebaseAuthUseCase,
) : ProfileUseCase {

    override suspend fun getUserData(): UserProfileDataModel? {
        val profileData = profileRepository.getUserData(
            firebaseAuthUseCase.getFirebaseAuth().currentUser?.uid ?: ""
        )
        return suspendCoroutine { continuation ->

            if (profileData.status == Status.SUCCESS) {
                continuation.resumeWith(Result.success(profileData.data))
            } else {
                continuation.resumeWith(Result.success(null))
            }
        }
    }
}