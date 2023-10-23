package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.util.model.StatusModel

interface ProfileUseCase {

    suspend fun verifyIfMovieIsFavorite(movieId: String): StatusModel

}