package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.repository.ProfileRepository
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.model.StatusModel
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class ProfileUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val firebaseAuth: FirebaseAuthUseCase,
) : ProfileUseCase {
    override suspend fun verifyIfMovieIsFavorite(movieId: String): StatusModel {
        val userData =
            profileRepository.getUserData(firebaseAuth.getFirebaseAuth().currentUser?.uid.toString())
        return suspendCoroutine { continuation ->
            if (userData.statusModel?.isValid == true) {
                if (userData.data?.favoriteMovies?.contains(movieId) == true) {
                    continuation.resumeWith(
                        Result.success(
                            StatusModel(
                                true,
                                null,
                                R.string.movie_added_to_your_favorites
                            )
                        )
                    )
                } else {
                    continuation.resumeWith(
                        Result.success(
                            StatusModel(
                                true,
                                null,
                                R.string.movie_removed_from_your_favorites
                            )
                        )
                    )
                }
            } else {
                continuation.resumeWith(
                    Result.success(
                        StatusModel(
                            false,
                            Constants.ERROR_TYPES.couldNotReachTheUserData,
                            R.string.error_login_user_not_found
                        )
                    )
                )
            }
        }
    }
}