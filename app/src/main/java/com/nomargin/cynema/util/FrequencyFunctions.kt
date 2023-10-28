package com.nomargin.cynema.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.MovieSearchedDetailsModel
import com.nomargin.cynema.data.remote.retrofit.entity.MovieModel
import com.nomargin.cynema.databinding.ActivityMovieDiscussionPostBinding
import com.nomargin.cynema.databinding.ItemDiscussionPostBinding
import com.nomargin.cynema.databinding.ItemMovieDiscussionPostCommentBinding
import com.nomargin.cynema.ui.fragment.favorites_fragment.FavoritesFragmentDirections
import com.nomargin.cynema.ui.fragment.favorites_fragment.FavoritesViewModel
import com.nomargin.cynema.ui.fragment.home_fragment.HomeFragmentDirections
import com.nomargin.cynema.ui.fragment.home_fragment.HomeViewModel
import com.nomargin.cynema.ui.fragment.search_fragment.SearchFragmentDirections
import com.nomargin.cynema.ui.fragment.search_fragment.SearchViewModel
import com.nomargin.cynema.util.model.CarouselModel
import com.nomargin.cynema.util.model.StatusModel
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object FrequencyFunctions {

    fun makeToast(context: Context, @StringRes message: Int) {
        Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
    }

    fun startNewActivityFromCurrentActivity(currentActivity: Activity, newActivity: Activity) {
        val intent = Intent(currentActivity, newActivity::class.java)
        currentActivity.startActivity(intent)
        currentActivity.finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPostDifferenceTime(timestamp: Timestamp?): String {
        Log.d("getPostDifferenceTime", "getPostDifferenceTime: ${timestamp?.seconds}")
        val postDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(timestamp!!.seconds, 0),
            ZoneId.systemDefault()
        )
        val currentDateTime = LocalDateTime.now()
        val diff = Duration.between(postDateTime, currentDateTime)
        return when {
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
    }

    fun <T : ViewBinding> updateVoteColors(
        item: T,
        context: Context,
        isUpVoted: Boolean,
        isDownVoted: Boolean,
    ) {

        when {
            isUpVoted -> {
                setVoteItems(
                    item,
                    context,
                    R.drawable.ic_up_voted,
                    R.drawable.ic_down_vote,
                    R.color.color_primary
                )
            }

            isDownVoted -> {
                setVoteItems(
                    item,
                    context,
                    R.drawable.ic_up_vote,
                    R.drawable.ic_down_voted,
                    R.color.red
                )
            }

            else -> {
                setVoteItems(
                    item,
                    context,
                    R.drawable.ic_up_vote,
                    R.drawable.ic_down_vote,
                    R.color.custom_black
                )
            }
        }
    }


    private fun <T : ViewBinding> setVoteItems(
        item: T,
        itemViewContext: Context,
        @DrawableRes upVoteRes: Int,
        @DrawableRes downVoteRes: Int,
        @ColorRes voteValueColor: Int,
    ) {
        when (item) {
            is ItemDiscussionPostBinding -> {
                item.run {
                    buttonUpVote.setImageDrawable(
                        AppCompatResources.getDrawable(
                            itemViewContext,
                            upVoteRes
                        )
                    )
                    buttonDownVote.setImageDrawable(
                        AppCompatResources.getDrawable(
                            itemViewContext,
                            downVoteRes
                        )
                    )
                    voteValue.setTextColor(
                        ContextCompat.getColor(itemViewContext, voteValueColor)
                    )
                }
            }

            is ActivityMovieDiscussionPostBinding -> {
                item.run {
                    buttonUpVote.setImageDrawable(
                        AppCompatResources.getDrawable(
                            itemViewContext,
                            upVoteRes
                        )
                    )
                    buttonDownVote.setImageDrawable(
                        AppCompatResources.getDrawable(
                            itemViewContext,
                            downVoteRes
                        )
                    )
                    voteValue.setTextColor(
                        ContextCompat.getColor(itemViewContext, voteValueColor)
                    )
                }
            }

            is ItemMovieDiscussionPostCommentBinding -> {
                item.run {
                    buttonUpVote.setImageDrawable(
                        AppCompatResources.getDrawable(
                            itemViewContext,
                            upVoteRes
                        )
                    )
                    buttonDownVote.setImageDrawable(
                        AppCompatResources.getDrawable(
                            itemViewContext,
                            downVoteRes
                        )
                    )
                    voteValue.setTextColor(
                        ContextCompat.getColor(itemViewContext, voteValueColor)
                    )
                }
            }
        }
    }

    fun incrementAndDecrementHandler(
        updateType: Constants.UPDATE_TYPE,
        votedType: Constants.VOTE_TYPE?,
        increment: Long,
        hasVoted: Boolean,
        postReference: DocumentReference,
        currentUserId: String?,
    ): Resource<StatusModel> {
        return if (hasVoted) {
            when (updateType) {
                Constants.UPDATE_TYPE.Upvote -> {
                    if (votedType == Constants.VOTE_TYPE.Upvote) {
                        //Removes the vote
                        postReference.update(
                            "votes",
                            FieldValue.increment(-1)
                        )
                        postReference.update(
                            Constants.FIRESTORE.usersWhoUpVoted,
                            FieldValue.arrayRemove(
                                currentUserId ?: ""
                            )
                        )
                        postReference.update(
                            Constants.FIRESTORE.usersWhoVoted,
                            FieldValue.arrayRemove(
                                currentUserId ?: ""
                            )
                        )


                    } else {
                        postReference.update(
                            "votes",
                            FieldValue.increment(2)
                        )
                        postReference.update(
                            Constants.FIRESTORE.usersWhoDownVoted,
                            FieldValue.arrayRemove(
                                currentUserId ?: ""
                            )
                        )
                        postReference.update(
                            Constants.FIRESTORE.usersWhoUpVoted,
                            FieldValue.arrayUnion(
                                currentUserId ?: ""
                            )
                        )
                        postReference.update(
                            Constants.FIRESTORE.usersWhoVoted,
                            FieldValue.arrayUnion(
                                currentUserId ?: ""
                            )
                        )
                    }
                }

                else -> {
                    if (votedType == Constants.VOTE_TYPE.Downvote) {
                        //Removes the vote
                        postReference.update(
                            "votes",
                            FieldValue.increment(1)
                        )
                        postReference.update(
                            Constants.FIRESTORE.usersWhoDownVoted,
                            FieldValue.arrayRemove(
                                currentUserId ?: ""
                            )
                        )
                        postReference.update(
                            Constants.FIRESTORE.usersWhoVoted,
                            FieldValue.arrayRemove(
                                currentUserId ?: ""
                            )
                        )
                    } else {
                        postReference.update(
                            "votes",
                            FieldValue.increment(-2)
                        )
                        postReference.update(
                            Constants.FIRESTORE.usersWhoDownVoted,
                            FieldValue.arrayUnion(
                                currentUserId ?: ""
                            )
                        )
                        postReference.update(
                            Constants.FIRESTORE.usersWhoUpVoted,
                            FieldValue.arrayRemove(
                                currentUserId ?: ""
                            )
                        )
                        postReference.update(
                            Constants.FIRESTORE.usersWhoVoted,
                            FieldValue.arrayUnion(
                                currentUserId ?: ""
                            )
                        )
                    }
                }
            }
            Resource.success(
                null,
                StatusModel(
                    true,
                    null,
                    R.string.comment_reached_with_success
                )
            )
        } else {
            when (updateType) {
                Constants.UPDATE_TYPE.Upvote -> {
                    postReference.update(
                        Constants.FIRESTORE.usersWhoUpVoted,
                        FieldValue.arrayUnion(
                            currentUserId ?: ""
                        )
                    )
                }

                else -> {
                    postReference.update(
                        Constants.FIRESTORE.usersWhoDownVoted,
                        FieldValue.arrayUnion(
                            currentUserId ?: ""
                        )
                    )
                }
            }
            postReference.update(
                Constants.FIRESTORE.usersWhoVoted,
                FieldValue.arrayUnion(
                    currentUserId ?: ""
                )
            )
            postReference.update(
                "votes",
                FieldValue.increment(increment)
            )
            Resource.success(
                null,
                StatusModel(
                    true,
                    null,
                    R.string.comment_reached_with_success
                )
            )
        }
    }

    fun <T> navigateToMovieDetails(
        transactionType: Int,
        item: T,
        activity: Activity,
        viewModel: ViewModel,
    ) {
        val navigate = when (transactionType) {
            Constants.CLASS_TYPE.carouselModel -> {
                val itemToCarousel = item as CarouselModel
                val viewModelType = viewModel as HomeViewModel
                viewModelType.saveDataToSharedPreferences(
                    Constants.LOCAL_STORAGE.sharedPreferencesMovieIdKey,
                    itemToCarousel.id.toString()
                )
                HomeFragmentDirections.actionHomeFragmentToMovieFragment()
            }

            Constants.CLASS_TYPE.movieModel -> {
                val itemToMovieModel = item as MovieModel
                val viewModelType = viewModel as HomeViewModel
                viewModelType.saveDataToSharedPreferences(
                    Constants.LOCAL_STORAGE.sharedPreferencesMovieIdKey,
                    itemToMovieModel.movieId.toString()
                )
                HomeFragmentDirections.actionHomeFragmentToMovieFragment()
            }

            Constants.CLASS_TYPE.movieSearchedDetailsModel -> {
                val itemToMovieModel = item as MovieSearchedDetailsModel
                val viewModelType = viewModel as SearchViewModel
                viewModelType.saveDataToSharedPreferences(
                    Constants.LOCAL_STORAGE.sharedPreferencesMovieIdKey,
                    itemToMovieModel.id.toString()
                )
                SearchFragmentDirections.actionSearchFragmentToMovieFragment()
            }

            else -> {
                val itemToMovieModel = item as MovieSearchedDetailsModel
                val viewModelType = viewModel as FavoritesViewModel
                viewModelType.saveDataToSharedPreferences(
                    Constants.LOCAL_STORAGE.sharedPreferencesMovieIdKey,
                    itemToMovieModel.id.toString()
                )
                FavoritesFragmentDirections.actionFavoritesFragmentToMovieFragment()
            }
        }
        activity
            .findNavController(
                R.id.fragmentContainerView
            )
            .navigate(
                navigate
            )
    }

}