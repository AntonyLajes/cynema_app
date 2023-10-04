package com.nomargin.cynema.data.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.data.repository.PostRepository
import com.nomargin.cynema.data.repository.ProfileRepository
import com.nomargin.cynema.util.FrequencyFunctions
import javax.inject.Inject

class PostUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository,
    private val profileRepository: ProfileRepository,
    private val theMovieDatabaseApiUseCase: TheMovieDatabaseApiUseCase,
) : PostUseCase {
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
                        commentsQuantity = commentsQuantity
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
                commentsQuantity = it.commentsQuantity.toString()
            )
        }
    }
}