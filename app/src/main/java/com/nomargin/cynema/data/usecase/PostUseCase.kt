package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.util.Constants

interface PostUseCase {

    suspend fun getPosts(movieId: String): List<PostAppearanceModel>
    suspend fun getPostById(postId: String): PostAppearanceModel?
    suspend fun updatePostVote(
        updateType: Constants.UPDATE_TYPE,
        postId: String
    ): PostAppearanceModel?

}