package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.data.local.entity.PostModel
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel

interface PostUseCase {

    suspend fun publishPost(postModel: PostModel): Resource<String>
    suspend fun deletePost(postId: String): StatusModel
    suspend fun getPosts(movieId: String): List<PostAppearanceModel>
    suspend fun getPostById(postId: String): PostAppearanceModel?
    suspend fun updatePostVote(
        updateType: Constants.UPDATE_TYPE,
        postId: String
    ): PostAppearanceModel?

    suspend fun getPostByIdList(
        postIdList: List<String>
    ): List<PostAppearanceModel>
}