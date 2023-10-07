package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.local.entity.CommentAppearanceModel
import com.nomargin.cynema.util.Constants

interface CommentUseCase {

    suspend fun getComments(postId: String): List<CommentAppearanceModel>
    suspend fun getCommentById(commentId: String): CommentAppearanceModel?
    suspend fun updateCommentVote(
        updateType: Constants.UPDATE_TYPE,
        commentId: String,
    ): CommentAppearanceModel?

}