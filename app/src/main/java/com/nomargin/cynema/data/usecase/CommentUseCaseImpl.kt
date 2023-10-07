package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.local.entity.CommentAppearanceModel
import com.nomargin.cynema.util.Constants
import javax.inject.Inject

class CommentUseCaseImpl @Inject constructor(
) : CommentUseCase {
    override suspend fun getComments(postId: String): List<CommentAppearanceModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getCommentById(commentId: String): CommentAppearanceModel? {
        TODO("Not yet implemented")
    }

    override suspend fun updateCommentVote(
        updateType: Constants.UPDATE_TYPE,
        commentId: String,
    ): CommentAppearanceModel? {
        TODO("Not yet implemented")
    }
}