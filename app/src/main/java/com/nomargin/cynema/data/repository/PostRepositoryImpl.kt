package com.nomargin.cynema.data.repository

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

class PostRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestoreUseCase,
    private val firebaseAuth: FirebaseAuthUseCase,
    private val validateAttributes: ValidateAttributesUseCase,
    private val profileRepository: ProfileRepository,
) : PostRepository {

    private lateinit var createPostResult: Resource<String>
    private lateinit var updateResult: Resource<String>
    private lateinit var deleteResult: StatusModel
    private val randomUUID = UUID.randomUUID().toString()

    override suspend fun publishPost(postModel: PostModel): Resource<String> {
        val database = firebaseFirestore.getFirebaseFirestore()
            .collection(Constants.FIRESTORE.postsCollection)
            .document(randomUUID)

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

            database.set(post)
                .addOnSuccessListener {
                    createPostResult = Resource.success(
                        randomUUID,
                        StatusModel(
                            true,
                            null,
                            R.string.profile_updated_with_success
                        )
                    )
                }.addOnFailureListener {
                    createPostResult = Resource.error(
                        "Error",
                        null,
                        StatusModel(
                            false,
                            null,
                            R.string.unknown_error
                        )
                    )
                }.addOnCompleteListener { }.await()
        } else {
            createPostResult = Resource.error(
                "Error",
                null,
                validatePostAttributes
            )
        }
        return createPostResult
    }

    override suspend fun getAllPosts(movieId: String): Resource<List<PostDatabaseModel>> {
        val posts = firebaseFirestore.getFirebaseFirestore()
            .collection(Constants.FIRESTORE.postsCollection)
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
        val post = firebaseFirestore.getFirebaseFirestore()
            .collection(Constants.FIRESTORE.postsCollection)
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
            firebaseFirestore
                .getFirebaseFirestore()
                .collection(Constants.FIRESTORE.postsCollection)
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

        val userReference = firebaseFirestore
            .getFirebaseFirestore()
            .collection(Constants.FIRESTORE.usersCollection)
            .document(firebaseAuth.getFirebaseAuth().currentUser?.uid ?: "")

        return if (userReference.get().addOnCompleteListener { }.await().exists()) {
            when (updateType) {
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

    override suspend fun updatePost(postModel: PostModel): Resource<String> {
        try {
            val postReference = firebaseFirestore
                .getFirebaseFirestore()
                .collection(Constants.FIRESTORE.postsCollection)
                .document(postModel.id ?: "")

            postReference
                .update(
                    mapOf(
                        "title" to postModel.title,
                        "body" to postModel.body,
                        "spoiler" to postModel.isSpoiler
                    )
                ).addOnCompleteListener {
                    updateResult = if (it.isSuccessful) {
                        Resource.success(
                            postModel.id,
                            StatusModel(
                                true,
                                null,
                                R.string.post_updated_with_successfully
                            )
                        )
                    } else {
                        Resource.error(
                            "Error",
                            null,
                            StatusModel(
                                false,
                                Constants.ERROR_TYPES.couldNotReachTheDiscussionPost,
                                R.string.error
                            )
                        )
                    }
                }.await()
        } catch (e: Exception) {
            updateResult = Resource.error(
                "Error",
                null,
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.couldNotReachTheDiscussionPost,
                    R.string.error
                )
            )
        }
        return updateResult
    }

    override suspend fun deletePost(postId: String): StatusModel {
        try {
            val postReference = firebaseFirestore
                .getFirebaseFirestore()
                .collection(Constants.FIRESTORE.postsCollection)
                .document(postId)

            postReference
                .update(
                    mapOf(
                        "active" to false
                    )
                ).addOnCompleteListener {
                    deleteResult = if (it.isSuccessful) {
                        StatusModel(
                            true,
                            null,
                            R.string.post_deleted_with_success
                        )
                    } else {
                        StatusModel(
                            false,
                            null,
                            R.string.discussion_posts_was_not_reached
                        )
                    }
                }.await()
        } catch (e: Exception) {
            deleteResult = StatusModel(
                false,
                null,
                R.string.unknown_error
            )
        }
        return deleteResult
    }
}