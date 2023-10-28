package com.nomargin.cynema.data.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.data.local.entity.PostModel
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.repository.PostRepository
import com.nomargin.cynema.data.repository.ProfileRepository
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.FrequencyFunctions
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.Status
import javax.inject.Inject

class PostUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository,
    private val profileRepository: ProfileRepository,
    private val theMovieDatabaseApiUseCase: TheMovieDatabaseApiUseCase,
    firebaseAuth: FirebaseAuthUseCase,
) : PostUseCase {
    private val currentUser = firebaseAuth.getFirebaseAuth().currentUser

    override suspend fun publishPost(postModel: PostModel): Resource<String> {
        val publishPostResult = postRepository.publishPost(postModel)
        return if (publishPostResult.status == Status.SUCCESS) {
            val updateResult = profileRepository.updateProfileWhenUserCreateAPoster(
                Constants.UPDATE_TYPE.AddPost,
                publishPostResult.data ?: ""
            )
            if (updateResult?.isValid == true) {
                publishPostResult
            } else {
                publishPostResult
            }
        } else {
            publishPostResult
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getPosts(movieId: String): List<PostAppearanceModel> {
        val posts: MutableList<PostAppearanceModel> = mutableListOf()
        val postsFromDatabase = postRepository.getAllPosts(movieId).data
        postsFromDatabase?.let {
            for (post in it) {
                val user = profileRepository.getUserData(post.userId).data
                val movie = theMovieDatabaseApiUseCase.getMovieDetails(movieId)
                val postedTime = FrequencyFunctions.getPostDifferenceTime(post.timestamp)

                val commentsQuantity =
                    if (post.commentsQuantity > 50) "50+" else post.commentsQuantity.toString()

                posts.add(
                    PostAppearanceModel(
                        postId = post.id,
                        user = user,
                        movie = movie,
                        title = post.title,
                        body = post.body,
                        isSpoiler = post.isSpoiler,
                        timestamp = postedTime,
                        votes = post.votes.toString(),
                        comments = post.comments,
                        commentsQuantity = commentsQuantity,
                        usersWhoVoted = post.usersWhoVoted,
                        usersWhoUpVoted = post.usersWhoUpVoted,
                        usersWhoDownVoted = post.usersWhoDownVoted,
                        currentUser = currentUser
                    )
                )
            }
        }
        return posts
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getPostById(postId: String): PostAppearanceModel? {
        val postFromDatabase = postRepository.getPostById(postId).data
        return postFromDatabase?.let {
            val user = profileRepository.getUserData(it.userId).data
            val movie = theMovieDatabaseApiUseCase.getMovieDetails(it.movieId)
            val postedTime = FrequencyFunctions.getPostDifferenceTime(it.timestamp)
            PostAppearanceModel(
                postId = it.id,
                user = user,
                movie = movie,
                title = it.title,
                body = it.body,
                isSpoiler = it.isSpoiler,
                timestamp = postedTime,
                votes = it.votes.toString(),
                comments = it.comments,
                commentsQuantity = it.commentsQuantity.toString(),
                usersWhoVoted = it.usersWhoVoted,
                usersWhoUpVoted = it.usersWhoUpVoted,
                usersWhoDownVoted = it.usersWhoDownVoted,
                currentUser = currentUser
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updatePostVote(
        updateType: Constants.UPDATE_TYPE,
        postId: String,
    ): PostAppearanceModel? {
        val updatePostVote = postRepository.updatePostVote(updateType, postId)
        return if (updatePostVote.status == Status.SUCCESS) {
            getPostById(postId)
        } else {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getPostByIdList(postIdList: List<String>): List<PostAppearanceModel> {
        val postList: MutableList<PostAppearanceModel> = mutableListOf()
        for (postId in postIdList) {
            getPostById(postId)?.let { postList.add(it) }
        }
        return postList
    }
}