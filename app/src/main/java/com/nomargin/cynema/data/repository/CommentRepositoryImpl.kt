package com.nomargin.cynema.data.repository

import com.google.firebase.firestore.FieldValue
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.CommentModel
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.remote.firebase.entity.CommentDatabaseModel
import com.nomargin.cynema.data.remote.firebase.firestore.FirebaseFirestoreUseCase
import com.nomargin.cynema.data.usecase.ValidateAttributesUseCase
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestoreUseCase,
    private val firebaseAuth: FirebaseAuthUseCase,
    private val validateAttributes: ValidateAttributesUseCase,
    private val profileRepository: ProfileRepository,
) : CommentRepository {

    private lateinit var createCommentResult: Resource<String>
    private val randomUUID = UUID.randomUUID().toString()

    override suspend fun publishComment(commentModel: CommentModel): Resource<String> {
        val validateCommentAttributes = validateAttributes.validateComment(commentModel)
        val postsDatabase = firebaseFirestore.getFirebaseFirestore()
            .collection(Constants.FIRESTORE.postsCollection)
            .document(commentModel.postId)
        val commentsDatabase = firebaseFirestore.getFirebaseFirestore()
            .collection(Constants.FIRESTORE.commentsCollection)
            .document(randomUUID)

        if (validateCommentAttributes.isValid) {

            val comment = CommentDatabaseModel(
                id = randomUUID,
                userId = firebaseAuth.getFirebaseAuth().currentUser?.uid.toString(),
                postId = commentModel.postId,
                body = commentModel.body,
                isSpoiler = commentModel.isSpoiler,
                votes = 0,
                usersWhoVoted = listOf(),
                answers = listOf(),
                usersWhoUpVoted = listOf(),
                usersWhoDownVoted = listOf(),
                answersQuantity = 0
            )

            commentsDatabase.set(comment)
                .addOnSuccessListener {
                    postsDatabase.update(
                        "comments",
                        FieldValue.arrayUnion(comment.id)
                    )
                    postsDatabase.update(
                        "commentsQuantity",
                        FieldValue.increment(1)
                    )
                    createCommentResult = Resource.success(
                        randomUUID,
                        StatusModel(
                            true,
                            null,
                            R.string.comment_created_with_success
                        )
                    )
                }.addOnFailureListener {
                    createCommentResult = Resource.error(
                        "Error",
                        null,
                        StatusModel(
                            false,
                            null,
                            R.string.unknown_error
                        )
                    )
                }.await()


        } else {
            createCommentResult = Resource.error(
                "Error",
                null,
                validateCommentAttributes
            )
        }
        return createCommentResult
    }

    override suspend fun getAllComments(commentId: String): Resource<List<CommentDatabaseModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCommentById(commentId: String): Resource<CommentDatabaseModel> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCommentVote(
        updateType: Constants.UPDATE_TYPE,
        commentId: String,
    ): Resource<StatusModel> {
        TODO("Not yet implemented")
    }
}