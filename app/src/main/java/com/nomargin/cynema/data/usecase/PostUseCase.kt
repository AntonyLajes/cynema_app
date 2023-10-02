package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.local.entity.PostAppearanceModel

interface PostUseCase {

    suspend fun getPosts(movieId: String): List<PostAppearanceModel>

}