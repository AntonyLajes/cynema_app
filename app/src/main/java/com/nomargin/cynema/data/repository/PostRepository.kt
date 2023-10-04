package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.local.entity.PostModel
import com.nomargin.cynema.data.remote.firebase.entity.PostDatabaseModel
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel

interface PostRepository {

    suspend fun publishPost(
        postModel: PostModel
    ): Resource<StatusModel>

    suspend fun getAllPosts(movieId: String): Resource<List<PostDatabaseModel>>
    suspend fun getPostById(postId: String): Resource<PostDatabaseModel>

}