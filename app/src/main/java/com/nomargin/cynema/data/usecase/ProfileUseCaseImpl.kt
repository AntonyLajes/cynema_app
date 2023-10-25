package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.MovieSearchedDetailsModel
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.repository.ProfileRepository
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class ProfileUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val theMovieDatabaseApiUseCase: TheMovieDatabaseApiUseCase,
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
                                R.string.movie_is_in_user_favorites
                            )
                        )
                    )
                } else {
                    continuation.resumeWith(
                        Result.success(
                            StatusModel(
                                true,
                                null,
                                R.string.movie_is_not_in_user_favorites
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

    override suspend fun getFavoriteMovies(): Resource<List<MovieSearchedDetailsModel>> {
        val userData =
            profileRepository.getUserData(firebaseAuth.getFirebaseAuth().currentUser?.uid.toString())
        return if (userData.statusModel?.isValid == true) {
            if (userData.data?.favoriteMovies?.isEmpty() == true) {
                return Resource.success(
                    null,
                    StatusModel(
                        true,
                        null,
                        R.string.user_has_not_movie_added_to_favorite_list
                    )
                )
            } else {
                val favoriteMovieDetails: MutableList<MovieSearchedDetailsModel> = mutableListOf()
                for (movieId in userData.data?.favoriteMovies!!) {
                    favoriteMovieDetails.add(
                        theMovieDatabaseApiUseCase.getMovieDetailsById(
                            movieId
                        )
                    )
                }
                return suspendCoroutine {
                    it.resumeWith(
                        Result.success(
                            Resource.success(
                                favoriteMovieDetails.toList(),
                                StatusModel(
                                    true,
                                    null,
                                    R.string.favorite_movies_successfully_got
                                )
                            )
                        )
                    )
                }
            }
        } else {
            Resource.error(
                "Error",
                null,
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.couldNotReachTheUserData,
                    R.string.error_login_user_not_found
                )
            )
        }
    }
}