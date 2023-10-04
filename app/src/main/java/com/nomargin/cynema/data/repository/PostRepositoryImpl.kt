package com.nomargin.cynema.data.repository

import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.PostModel
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.remote.firebase.entity.PostDatabaseModel
import com.nomargin.cynema.data.remote.firebase.firestore.FirebaseFirestoreUseCase
import com.nomargin.cynema.data.usecase.ValidateAttributesUseCase
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestoreUseCase,
    private val firebaseAuth: FirebaseAuthUseCase,
    private val validateAttributes: ValidateAttributesUseCase,
) : PostRepository {

    private lateinit var createPostResult: Resource<StatusModel>
    private val randomUUID = UUID.randomUUID().toString()

    override suspend fun publishPost(postModel: PostModel): Resource<StatusModel> {
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
                comments = listOf(),
                commentsQuantity = 0
            )

            database.set(post)
                .addOnSuccessListener {
                    createPostResult = Resource.success(
                        null,
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
                }.await()

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
}