package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.local.PostModel
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel

interface PostRepository {

    suspend fun publishPost(
        post: PostModel
    ): Resource<StatusModel>

}