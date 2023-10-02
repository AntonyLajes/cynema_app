package com.nomargin.cynema.data.repository

import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.PostModel
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.remote.firebase.firestore.FirebaseFirestoreUseCase
import com.nomargin.cynema.data.remote.retrofit.entity.PostDatabaseModel
import com.nomargin.cynema.data.usecase.ValidateAttributesUseCase
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel
import kotlinx.coroutines.tasks.await
import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    firebaseFirestore: FirebaseFirestoreUseCase,
    private val firebaseAuth: FirebaseAuthUseCase,
    private val validateAttributes: ValidateAttributesUseCase,
) : PostRepository {

    private lateinit var createPostResult: Resource<StatusModel>
    private val randomUUID = UUID.randomUUID().toString()
    private val database = firebaseFirestore.getFirebaseFirestore()
        .collection(Constants.FIRESTORE.postsCollection)
        .document(randomUUID)

    override suspend fun publishPost(postModel: PostModel): Resource<StatusModel> {
        val validatePostAttributes = validateAttributes.validatePost(postModel)
        if (validatePostAttributes.isValid) {

            val currentTimeMillis = System.currentTimeMillis()
            val timeStamp = Timestamp(currentTimeMillis)

            val post = PostDatabaseModel(
                id = randomUUID,
                userId = firebaseAuth.getFirebaseAuth().currentUser?.uid.toString(),
                movieId = postModel.movieId,
                title = postModel.title,
                body = postModel.body,
                isSpoiler = postModel.isSpoiler,
                timestamp = timeStamp,
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
}