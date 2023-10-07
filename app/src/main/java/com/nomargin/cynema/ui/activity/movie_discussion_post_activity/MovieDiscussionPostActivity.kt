package com.nomargin.cynema.ui.activity.movie_discussion_post_activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.databinding.ActivityMovieDiscussionPostBinding
import com.nomargin.cynema.ui.fragment.create_comment_post_sheet_fragment.CreateCommentPostBottomSheetFragment
import com.nomargin.cynema.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDiscussionPostActivity : AppCompatActivity(), View.OnClickListener {

    private var movieDiscussionPostId: String? = null
    private val binding: ActivityMovieDiscussionPostBinding by lazy {
        ActivityMovieDiscussionPostBinding.inflate(layoutInflater)
    }
    private val movieDiscussionPostViewModel: MovieDiscussionPostViewModel by viewModels()
    private lateinit var postAppearanceModel: PostAppearanceModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observers()
        getMovieDiscussionPostId()
        initClicks()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.addComment.id -> {
                val createCommentPostBottomSheetFragment = CreateCommentPostBottomSheetFragment()
                createCommentPostBottomSheetFragment.show(
                    this.supportFragmentManager,
                    "createCommentPostBottomSheetFragment"
                )
            }

            binding.buttonUpVote.id -> {
                movieDiscussionPostViewModel.updatePostVote(
                    Constants.UPDATE_TYPE.Upvote,
                    postAppearanceModel.postId
                )
            }

            binding.buttonDownVote.id -> {
                movieDiscussionPostViewModel.updatePostVote(
                    Constants.UPDATE_TYPE.Downvote,
                    postAppearanceModel.postId
                )
            }
        }
    }

    private fun initClicks() {
        binding.addComment.setOnClickListener(this)
        binding.buttonUpVote.setOnClickListener(this)
        binding.buttonDownVote.setOnClickListener(this)
    }

    private fun getMovieDiscussionPostId() {
        val bundle = intent.extras
        if (bundle != null) {
            movieDiscussionPostId = bundle.getString(
                Constants.BUNDLE_KEYS.MovieDiscussionPostId.toString()
            )
            movieDiscussionPostViewModel.getDiscussionPostById(movieDiscussionPostId)
        }
    }

    private fun observers() {
        movieDiscussionPostViewModel.post.observe(this) { post ->
            post?.let {
                fieldsHandler(it)
                postAppearanceModel = it
            }
        }
        movieDiscussionPostViewModel.getUpdatedPost.observe(this) { updatedPost ->
            updatedPost?.let {
                fieldsHandler(it)
            }
        }
    }

    private fun fieldsHandler(postDatabaseModel: PostAppearanceModel) {
        binding.postDiscussionTitle.text = postDatabaseModel.title
        binding.postDiscussionBody.text = postDatabaseModel.body
        binding.voteValue.text = postDatabaseModel.votes
        if (postDatabaseModel.usersWhoVoted.contains(postDatabaseModel.currentUser?.uid ?: "")) {
            val isUpVoted = postDatabaseModel.usersWhoUpVoted.contains(
                postDatabaseModel.currentUser?.uid ?: ""
            )
            val isDownVoted = postDatabaseModel.usersWhoDownVoted.contains(
                postDatabaseModel.currentUser?.uid ?: ""
            )
            updateVoteColors(isUpVoted, isDownVoted)
        } else {
            updateVoteColors(isUpVoted = false, isDownVoted = false)
        }
        binding.answersQuantity.text = postDatabaseModel.commentsQuantity
        binding.textAnswersQuantity.visibility =
            when (postDatabaseModel.commentsQuantity.toInt()) {
                0 -> {
                    View.GONE
                }

                1 -> {
                    binding.textAnswersQuantity.text = buildString {
                        append(postDatabaseModel.commentsQuantity)
                        append(" ")
                        append(getString(R.string.comment))
                    }
                    View.VISIBLE
                }

                else -> {
                    binding.textAnswersQuantity.text = buildString {
                        append(postDatabaseModel.commentsQuantity)
                        append(" ")
                        append(getString(R.string.comments))
                    }
                    View.VISIBLE
                }
            }
        binding.postOwner.text = buildString {
            append(" ")
            append(postDatabaseModel.user?.firstName)
            append(" ")
            append(postDatabaseModel.user?.lastName)
        }
        Glide.with(this)
            .load(postDatabaseModel.user?.profilePicture)
            .error(R.drawable.pic_profile_picture)
            .into(binding.profilePicture)

        binding.postDate.text = postDatabaseModel.timestamp
    }

    private fun updateVoteColors(isUpVoted: Boolean, isDownVoted: Boolean) {
        when {
            isUpVoted -> {
                setVoteItems(
                    R.drawable.ic_up_voted,
                    R.drawable.ic_down_vote,
                    R.color.color_primary
                )
            }

            isDownVoted -> {
                setVoteItems(
                    R.drawable.ic_up_vote,
                    R.drawable.ic_down_voted,
                    R.color.red
                )
            }

            else -> {
                setVoteItems(
                    R.drawable.ic_up_vote,
                    R.drawable.ic_down_vote,
                    R.color.custom_black
                )
            }
        }
    }

    private fun setVoteItems(
        @DrawableRes upVoteRes: Int,
        @DrawableRes downVoteRes: Int,
        @ColorRes voteValueColor: Int,
    ) {
        binding.buttonUpVote.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                upVoteRes
            )
        )
        binding.buttonDownVote.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                downVoteRes
            )
        )
        binding.voteValue.setTextColor(
            ContextCompat.getColor(this, voteValueColor)
        )
    }
}
