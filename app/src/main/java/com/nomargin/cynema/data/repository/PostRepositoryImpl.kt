package com.nomargin.cynema.data.repository

import com.nomargin.cynema.BuildConfig
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.PostModel
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.remote.firebase.entity.PostDatabaseModel
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

class PostRepositoryImpl @Inject constructor(
    firebaseFirestore: FirebaseFirestoreUseCase,
    private val firebaseAuth: FirebaseAuthUseCase,
    private val validateAttributes: ValidateAttributesUseCase,
    private val profileRepository: ProfileRepository,
) : PostRepository {

    private val randomUUID = UUID.randomUUID().toString()
    private val database = firebaseFirestore.getFirebaseFirestore()
        .collection("${Constants.FIRESTORE.rootCollection}/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}/${Constants.FIRESTORE.postsCollection}")

    override suspend fun publishPost(postModel: PostModel): Resource<String> {

        return suspendCoroutine { continuation ->

            val validatePostAttributes = validateAttributes.validatePost(postModel)

            if (validatePostAttributes.isValid) {

                val post = PostDatabaseModel(
                    id = randomUUID,
                    userId = firebaseAuth.getFirebaseAuth().currentUser?.uid.toString(),
                    movieId = postModel.movieId,
                    title = postModel.title,
                    body = postModel.body,
                    isSpoiler = postModel.isSpoiler,
                    votes = 0,
                    usersWhoVoted = listOf(),
                    comments = listOf(),
                    usersWhoUpVoted = listOf(),
                    usersWhoDownVoted = listOf(),
                    commentsQuantity = 0,
                    isActive = true
                )

                database.document(randomUUID).set(post)
                    .addOnSuccessListener {
                        continuation.resumeWith(
                            Result.success(
                                Resource.success(
                                    randomUUID,
                                    StatusModel(
                                        true,
                                        null,
                                        R.string.profile_updated_with_success
                                    )
                                )
                            )
                        )
                    }.addOnFailureListener {
                        continuation.resumeWith(
                            Result.failure(it)
                        )
                    }
            } else {
                continuation.resumeWith(
                    Result.success(
                        Resource.error(
                            "Error",
                            null,
                            validatePostAttributes
                        )
                    )
                )
            }

        }
    }

    override suspend fun getAllPosts(movieId: String): Resource<List<PostDatabaseModel>> {
        val posts = database
            .whereEqualTo("movieId", movieId)
            .whereEqualTo("active", true)
            .get()
            .addOnCompleteListener { }.await()
        return if (posts.isEmpty) {
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
                posts.toObjects(PostDatabaseModel::class.java),
                StatusModel(
                    true,
                    null,
                    R.string.discussion_posts_reached_with_success
                )
            )
        }
    }

    override suspend fun getPostById(postId: String): Resource<PostDatabaseModel> {
        val post = database
            .whereEqualTo("id", postId)
            .whereEqualTo("active", true)
            .get()
            .addOnCompleteListener { }.await()
        return if (post.isEmpty) {
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
                post.documents[0].toObject(PostDatabaseModel::class.java),
                StatusModel(
                    true,
                    null,
                    R.string.discussion_posts_reached_with_success
                )
            )
        }
    }

    override suspend fun updatePostVote(
        updateType: Constants.UPDATE_TYPE,
        postId: String,
    ): Resource<StatusModel> {
        val userData = profileRepository.getUserData(
            firebaseAuth.getFirebaseAuth().currentUser?.uid ?: ""
        )
        val postData = getPostById(postId)
        val postReference =
            database
                .document(postId)
        var hasVoted = false
        var votedType: Constants.VOTE_TYPE? = null
        if (postData.status == Status.SUCCESS) {
            for (postVote in postData.data!!.usersWhoVoted) {
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
                    Constants.ERROR_TYPES.couldNotReachTheDiscussionPost,
                    R.string.discussion_posts_was_not_reached
                )
            )
        }

        if (hasVoted) {
            when {
                postData.data.usersWhoUpVoted.contains(
                    firebaseAuth.getFirebaseAuth().currentUser?.uid ?: ""
                ) -> {
                    votedType = Constants.VOTE_TYPE.Upvote
                }

                postData.data.usersWhoDownVoted.contains(
                    firebaseAuth.getFirebaseAuth().currentUser?.uid ?: ""
                ) -> {
                    votedType = Constants.VOTE_TYPE.Downvote
                }
            }
        }

        return suspendCoroutine { continuation ->

            database.document(
                firebaseAuth.getFirebaseAuth().currentUser?.uid ?: ""
            ).get()
                .addOnSuccessListener {
                    val result = when (updateType) {
                        Constants.UPDATE_TYPE.Upvote -> {
                            FrequencyFunctions.incrementAndDecrementHandler(
                                updateType,
                                votedType,
                                1,
                                hasVoted,
                                postReference,
                                firebaseAuth.getFirebaseAuth().currentUser?.uid
                            )
                        }

                        else -> {
                            FrequencyFunctions.incrementAndDecrementHandler(
                                updateType,
                                votedType,
                                -1,
                                hasVoted,
                                postReference,
                                firebaseAuth.getFirebaseAuth().currentUser?.uid
                            )
                        }
                    }

                    continuation.resumeWith(
                        Result.success(
                            result
                        )
                    )

                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }
    }

    override suspend fun updatePost(postModel: PostModel): Resource<String> {
        return suspendCoroutine { continuation ->
            try {
                val postReference = database
                    .document(postModel.id ?: "")

                postReference
                    .update(
                        mapOf(
                            "title" to postModel.title,
                            "body" to postModel.body,
                            "spoiler" to postModel.isSpoiler
                        )
                    )
                    .addOnSuccessListener {
                        continuation.resumeWith(
                            Result.success(
                                Resource.success(
                                    postModel.id,
                                    StatusModel(
                                        true,
                                        null,
                                        R.string.post_updated_with_successfully
                                    )
                                )
                            )
                        )
                    }
                    .addOnFailureListener {
                        continuation.resumeWith(Result.failure(it))
                    }
            } catch (e: Exception) {
                continuation.resumeWith(Result.failure(e))
            }
        }
    }

    override suspend fun deletePost(postId: String): StatusModel {
        return suspendCoroutine { continuation ->
            try {
                val postReference = database
                    .document(postId)

                postReference
                    .update(
                        mapOf(
                            "active" to false
                        )
                    ).addOnSuccessListener {
                        continuation.resumeWith(
                            Result.success(
                                StatusModel(
                                    true,
                                    null,
                                    R.string.post_deleted_with_success
                                )
                            )
                        )
                    }
                    .addOnFailureListener {
                        continuation.resumeWith(Result.failure(it))
                    }
            } catch (e: Exception) {
                continuation.resumeWith(Result.failure(e))
            }
        }
    }
}