package com.nomargin.cynema.data.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.nomargin.cynema.data.local.entity.CommentAppearanceModel
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.repository.CommentRepository
import com.nomargin.cynema.data.repository.PostRepository
import com.nomargin.cynema.data.repository.ProfileRepository
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.FrequencyFunctions
import com.nomargin.cynema.util.Status
import javax.inject.Inject

class CommentUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val profileRepository: ProfileRepository,
    firebaseAuth: FirebaseAuthUseCase,
) : CommentUseCase {

    private val currentUser = firebaseAuth.getFirebaseAuth().currentUser

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAllComments(postId: String): List<CommentAppearanceModel> {
        val comments: MutableList<CommentAppearanceModel> = mutableListOf()
        val commentsFromDatabase = commentRepository.getAllComments(postId).data
        commentsFromDatabase?.let {
            for (comment in it) {
                val user = profileRepository.getUserData(comment.userId).data
                val post = postRepository.getPostById(comment.postId)
                val postedTime = FrequencyFunctions.getPostDifferenceTime(comment.timestamp)
                val answersQuantity =
                    if (comment.answersQuantity > 10) "10+" else comment.answersQuantity.toString()

                comments.add(
                    CommentAppearanceModel(
                        commentId = comment.id,
                        user = user,
                        post = post.data,
                        body = comment.body,
                        isSpoiler = comment.isSpoiler,
                        timestamp = postedTime,
                        votes = comment.votes.toString(),
                        answers = comment.answers,
                        answersQuantity = answersQuantity,
                        usersWhoVoted = comment.usersWhoVoted,
                        usersWhoUpVoted = comment.usersWhoUpVoted,
                        usersWhoDownVoted = comment.usersWhoDownVoted,
                        currentUser = currentUser,
                        active = comment.isActive
                    )
                )
            }
        }
        return comments
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getCommentById(commentId: String): CommentAppearanceModel? {
        val commentFromDatabase = commentRepository.getCommentById(commentId).data
        return commentFromDatabase?.let {
            val user = profileRepository.getUserData(it.userId).data
            val post = postRepository.getPostById(it.postId)
            val postedTime = FrequencyFunctions.getPostDifferenceTime(it.timestamp)
            CommentAppearanceModel(
                commentId = it.id,
                user = user,
                post = post.data,
                body = it.body,
                isSpoiler = it.isSpoiler,
                timestamp = postedTime,
                votes = it.votes.toString(),
                answers = it.answers,
                answersQuantity = it.answersQuantity.toString(),
                usersWhoVoted = it.usersWhoVoted,
                usersWhoUpVoted = it.usersWhoUpVoted,
                usersWhoDownVoted = it.usersWhoDownVoted,
                currentUser = currentUser
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateCommentVote(
        updateType: Constants.UPDATE_TYPE,
        commentId: String,
    ): CommentAppearanceModel? {
        val updateCommentVote = commentRepository.updateCommentVote(updateType, commentId)
        return if (updateCommentVote.status == Status.SUCCESS) {
            getCommentById(commentId)
        } else {
            null
        }
    }
}