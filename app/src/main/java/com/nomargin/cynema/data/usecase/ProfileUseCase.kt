package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.remote.firebase.entity.UserProfileDataModel
import com.nomargin.cynema.data.local.entity.MovieSearchedDetailsModel
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel

interface ProfileUseCase {
   suspend fun getUserData(): UserProfileDataModel?

    suspend fun verifyIfMovieIsFavorite(movieId: String): StatusModel

    suspend fun getFavoriteMovies(): Resource<List<MovieSearchedDetailsModel>>
}