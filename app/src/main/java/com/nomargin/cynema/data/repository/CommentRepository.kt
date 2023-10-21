package com.nomargin.cynema.data.repository

import com.nomargin.cynema.data.local.entity.CommentModel
import com.nomargin.cynema.data.remote.firebase.entity.CommentDatabaseModel
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel

interface CommentRepository {

    suspend fun publishComment(
        commentModel: CommentModel,
    ): Resource<String>

    suspend fun getAllComments(postId: String): Resource<List<CommentDatabaseModel>>

    suspend fun getCommentById(commentId: String): Resource<CommentDatabaseModel>

    suspend fun updateCommentVote(
        updateType: Constants.UPDATE_TYPE,
        commentId: String,
    ): Resource<StatusModel>

    suspend fun updateComment(commentModel: CommentModel): Resource<String>
    suspend fun deleteComment(commentId: String): StatusModel?
}