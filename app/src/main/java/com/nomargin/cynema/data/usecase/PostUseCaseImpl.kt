package com.nomargin.cynema.data.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.data.repository.PostRepository
import com.nomargin.cynema.data.repository.ProfileRepository
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
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
                val postDateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(post.timestamp!!.seconds, 0),
                    ZoneId.systemDefault()
                )
                val currentDateTime = LocalDateTime.now()
                val diff = Duration.between(postDateTime, currentDateTime)
                val postedTime = when {
                    diff.toDays() >= 1 -> {
                        if (diff.toDays().toInt() >= 365) {
                            buildString {
                                append(
                                    postDateTime
                                        .month.name.lowercase().take(3)
                                        .replaceFirstChar { firstChar ->
                                            firstChar.uppercase()
                                        }
                                )
                                append(" ")
                                append(postDateTime.dayOfMonth)
                                append(", ")
                                append(postDateTime.year)
                            }
                        } else {
                            buildString {
                                append(
                                    postDateTime
                                        .month.name.lowercase().take(3)
                                        .replaceFirstChar { firstChar ->
                                            firstChar.uppercase()
                                        }
                                )
                                append(" ")
                                append(postDateTime.dayOfMonth)
                            }
                        }
                    }

                    diff.toHours() >= 1 -> {
                        "${diff.toHours()}hr"
                    }

                    else -> {
                        "${diff.toMinutes()}m"
                    }
                }

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
}