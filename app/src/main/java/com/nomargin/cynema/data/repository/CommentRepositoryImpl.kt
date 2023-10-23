package com.nomargin.cynema.data.repository

import com.google.firebase.firestore.FieldValue
import com.nomargin.cynema.BuildConfig
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.CommentModel
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.remote.firebase.entity.CommentDatabaseModel
import com.nomargin.cynema.data.remote.firebase.firestore.FirebaseFirestoreUseCase
import com.nomargin.cynema.data.usecase.ValidateAttributesUseCase
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.FrequencyFunctions
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.Status
import com.nomargin.cynema.util.model.StatusModel
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class CommentRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestoreUseCase,
    private val firebaseAuth: FirebaseAuthUseCase,
    private val validateAttributes: ValidateAttributesUseCase,
    private val profileRepository: ProfileRepository,
) : CommentRepository {

    private val randomUUID = UUID.randomUUID().toString()
    private val commentDatabase = firebaseFirestore.getFirebaseFirestore()
        .collection("${Constants.FIRESTORE.rootCollection}/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}/${Constants.FIRESTORE.commentsCollection}")
    private val postDatabase = firebaseFirestore.getFirebaseFirestore()
        .collection("${Constants.FIRESTORE.rootCollection}/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}/${Constants.FIRESTORE.postsCollection}")

    override suspend fun publishComment(commentModel: CommentModel): Resource<String> {
        return suspendCoroutine { continuation ->
            val validateCommentAttributes = validateAttributes.validateComment(commentModel)
            val postsDatabase = postDatabase
                .document(commentModel.postId)
            val commentsDatabase = commentDatabase
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
                    answersQuantity = 0,
                    isActive = true
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
                        continuation.resumeWith(
                            Result.success(
                                Resource.success(
                                    randomUUID,
                                    StatusModel(
                                        true,
                                        null,
                                        R.string.comment_created_with_success
                                    )
                                )
                            )
                        )
                    }.addOnFailureListener {
                        continuation.resumeWith(Result.failure(it))
                    }


            } else {
                continuation.resumeWith(
                    Result.success(
                        Resource.error(
                            "Error",
                            null,
                            validateCommentAttributes
                        )
                    )
                )
            }
        }
    }

    override suspend fun getAllComments(postId: String): Resource<List<CommentDatabaseModel>> {
        val comments = commentDatabase
            .whereEqualTo("postId", postId)
            .whereEqualTo("active", true)
            .get()
            .addOnCompleteListener { }.await()
        return if (comments.isEmpty) {
            Resource.error(
                "Error",
                null,
                StatusModel(
                    false,
                    null,
                    R.string.unknown_error
                )
            )
        } else {
            Resource.success(
                comments.toObjects(CommentDatabaseModel::class.java),
                StatusModel(
                    true,
                    null,
                    R.string.comments_reached_with_success
                )
            )
        }
    }

    override suspend fun getCommentById(commentId: String): Resource<CommentDatabaseModel> {
        val comment = commentDatabase
            .whereEqualTo("id", commentId)
            .get()
            .addOnCompleteListener { }.await()
        return if (comment.isEmpty) {
            Resource.error(
                "Error",
                null,
                StatusModel(
                    false,
                    null,
                    R.string.unknown_error
                )
            )
        } else {
            Resource.success(
                comment.documents[0].toObject(CommentDatabaseModel::class.java),
                StatusModel(
                    true,
                    null,
                    R.string.comment_reached_with_success
                )
            )
        }
    }

    override suspend fun updateCommentVote(
        updateType: Constants.UPDATE_TYPE,
        commentId: String,
    ): Resource<StatusModel> {
        val userData = profileRepository.getUserData(
            firebaseAuth.getFirebaseAuth().currentUser?.uid ?: ""
        )
        val commentData = getCommentById(commentId)
        val commentReference =
            commentDatabase
                .document(commentId)
        var hasVoted = false
        var votedType: Constants.VOTE_TYPE? = null
        if (commentData.status == Status.SUCCESS) {
            for (postVote in commentData.data!!.usersWhoVoted) {
                if (postVote == userData.data!!.id) {
                    hasVoted = true
                }
            }
        } else {
            return Resource.error(
                "Error",
                null,
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.couldNotReachTheDiscussionPostComment,
                    R.string.discussion_posts_was_not_reached
                )
            )
        }

        commentData.data.let {
            if (hasVoted) {
                when {
                    it.usersWhoUpVoted.contains(
                        firebaseAuth.getFirebaseAuth().currentUser?.uid ?: ""
                    ) -> {
                        votedType = Constants.VOTE_TYPE.Upvote
                    }

                    it.usersWhoDownVoted.contains(
                        firebaseAuth.getFirebaseAuth().currentUser?.uid ?: ""
                    ) -> {
                        votedType = Constants.VOTE_TYPE.Downvote
                    }
                }
            }
        }

        val userReference = firebaseFirestore
            .getFirebaseFirestore()
            .collection("${Constants.FIRESTORE.rootCollection}/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}/${Constants.FIRESTORE.usersCollection}")
            .document(firebaseAuth.getFirebaseAuth().currentUser?.uid ?: "")

        return if (userReference.get().addOnCompleteListener { }.await().exists()) {
            when (updateType) {
                Constants.UPDATE_TYPE.Upvote -> {
                    FrequencyFunctions.incrementAndDecrementHandler(
                        updateType,
                        votedType,
                        1,
                        hasVoted,
                        commentReference,
                        firebaseAuth.getFirebaseAuth().currentUser?.uid
                    )
                }

                else -> {
                    FrequencyFunctions.incrementAndDecrementHandler(
                        updateType,
                        votedType,
                        -1,
                        hasVoted,
                        commentReference,
                        firebaseAuth.getFirebaseAuth().currentUser?.uid
                    )
                }
            }
        } else {
            Resource.error(
                "Error",
                null,
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.userIsNotLoggedIn,
                    R.string.user_is_not_logged_in
                )
            )
        }

    }

    override suspend fun updateComment(commentModel: CommentModel): Resource<String> {
        return suspendCoroutine { continuation ->
            try {
                val commentReference = commentDatabase
                    .document(commentModel.id ?: "")

                commentReference
                    .update(
                        mapOf(
                            "body" to commentModel.body,
                            "spoiler" to commentModel.isSpoiler
                        )
                    ).addOnSuccessListener {
                        continuation.resumeWith(
                            Result.success(
                                Resource.success(
                                    commentModel.id,
                                    StatusModel(
                                        true,
                                        null,
                                        R.string.comment_updated_with_successfully
                                    )
                                )
                            )
                        )
                    }.addOnFailureListener {
                        continuation.resumeWith(Result.failure(it))
                    }

            } catch (e: Exception) {
                continuation.resumeWith(
                    Result.success(
                        Resource.error(
                            "Error",
                            null,
                            StatusModel(
                                false,
                                Constants.ERROR_TYPES.couldNotReachTheDiscussionPostComment,
                                R.string.error
                            )
                        )
                    )
                )
            }
        }
    }

    override suspend fun deleteComment(commentId: String): StatusModel {
        val comment = getCommentById(commentId)
        val postRef =
            firebaseFirestore.getFirebaseFirestore()
                .collection("${Constants.FIRESTORE.rootCollection}/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}/${Constants.FIRESTORE.postsCollection}")
                .document(comment.data?.postId ?: "")
        postRef.update(
            "comments",
            FieldValue.arrayRemove(comment.data?.id ?: "")
        ).continueWithTask {
            postRef.update(
                "commentsQuantity",
                FieldValue.increment(-1)
            )
        }
        return suspendCoroutine { continuation ->
            commentDatabase
                .document(commentId)
                .update(
                    "active",
                    false
                ).addOnSuccessListener {
                    continuation.resumeWith(
                        Result.success(
                            StatusModel(
                                true,
                                null,
                                R.string.comment_deleted_with_success
                            )
                        )
                    )
                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }
    }
}