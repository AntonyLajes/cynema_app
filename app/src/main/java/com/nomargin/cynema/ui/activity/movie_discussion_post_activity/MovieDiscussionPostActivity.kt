package com.nomargin.cynema.ui.activity.movie_discussion_post_activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.databinding.ActivityMovieDiscussionPostBinding
import com.nomargin.cynema.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDiscussionPostActivity : AppCompatActivity() {

    private var movieDiscussionPostId: String? = null
    private val binding: ActivityMovieDiscussionPostBinding by lazy {
        ActivityMovieDiscussionPostBinding.inflate(layoutInflater)
    }
    private val movieDiscussionPostViewModel: MovieDiscussionPostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observers()
        getMovieDiscussionPostId()
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
            }
        }
    }

    private fun fieldsHandler(postDatabaseModel: PostAppearanceModel) {
        binding.postDiscussionTitle.text = postDatabaseModel.title
        binding.postDiscussionBody.text = postDatabaseModel.body
        binding.voteValue.text = postDatabaseModel.votes
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
}