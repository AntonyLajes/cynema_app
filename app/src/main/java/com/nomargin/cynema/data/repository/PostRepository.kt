package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.local.entity.PostModel
import com.nomargin.cynema.data.remote.firebase.entity.PostDatabaseModel
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel

interface PostRepository {

    suspend fun publishPost(
        postModel: PostModel
    ): Resource<String>

    suspend fun getAllPosts(movieId: String): Resource<List<PostDatabaseModel>>
    suspend fun getPostById(postId: String): Resource<PostDatabaseModel>
    suspend fun updatePostVote(updateType: Constants.UPDATE_TYPE, postId: String): Resource<StatusModel>
    suspend fun updatePost(postModel: PostModel): Resource<String>
}