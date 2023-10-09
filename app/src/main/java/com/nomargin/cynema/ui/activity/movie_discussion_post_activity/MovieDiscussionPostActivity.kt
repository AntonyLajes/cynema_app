package com.nomargin.cynema.ui.activity.movie_discussion_post_activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.CommentAppearanceModel
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.databinding.ActivityMovieDiscussionPostBinding
import com.nomargin.cynema.ui.adapter.recycler_view.MovieDiscussionPostCommentAdapter
import com.nomargin.cynema.ui.fragment.create_comment_post_sheet_fragment.CreateCommentPostBottomSheetFragment
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.FrequencyFunctions
import com.nomargin.cynema.util.extension.AdapterOnItemClickListenerWithView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDiscussionPostActivity : AppCompatActivity(), View.OnClickListener {

    private var movieDiscussionPostId: String? = null
    private val binding: ActivityMovieDiscussionPostBinding by lazy {
        ActivityMovieDiscussionPostBinding.inflate(layoutInflater)
    }
    private val movieDiscussionPostViewModel: MovieDiscussionPostViewModel by viewModels()
    private lateinit var postAppearanceModel: PostAppearanceModel
    private lateinit var movieDiscussionPostCommentAdapter: MovieDiscussionPostCommentAdapter
    private var itemPositionPost: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observers()
        initMovieDiscussionPostCommentRecyclerView()
        getMovieDiscussionPostId()
        initClicks()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.addComment.id -> {
                val bundle = Bundle()
                bundle.putString(
                    Constants.BUNDLE_KEYS.MovieDiscussionPostOwnerName.name,
                    buildString {
                        append(postAppearanceModel.user?.firstName)
                        append(" ")
                        append(postAppearanceModel.user?.lastName)
                    }
                )
                val createCommentPostBottomSheetFragment = CreateCommentPostBottomSheetFragment()
                createCommentPostBottomSheetFragment.arguments = bundle
                createCommentPostBottomSheetFragment.show(
                    this.supportFragmentManager, "createCommentPostBottomSheetFragment"
                )
            }

            binding.buttonUpVote.id -> {
                movieDiscussionPostViewModel.updatePostVote(
                    Constants.UPDATE_TYPE.Upvote, postAppearanceModel.postId
                )
            }

            binding.buttonDownVote.id -> {
                movieDiscussionPostViewModel.updatePostVote(
                    Constants.UPDATE_TYPE.Downvote, postAppearanceModel.postId
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
            movieDiscussionPostViewModel.getAllComments(movieDiscussionPostId)
        }
    }

    private fun observers() {
        movieDiscussionPostViewModel.post.observe(this) { post ->
            post?.let {
                fieldsHandler(it)
                postAppearanceModel = it
                saveMovieDiscussionPostId()
            }
        }
        movieDiscussionPostViewModel.getUpdatedPost.observe(this) { updatedPost ->
            updatedPost?.let {
                fieldsHandler(it)
            }
        }
        movieDiscussionPostViewModel.getComments.observe(this) { comments ->
            comments?.let {
                updateMovieDiscussionPostCommentRecyclerView(comments)
            }
        }
        movieDiscussionPostViewModel.getUpdatedComment.observe(this){updatedComment ->
            updatedComment?.let {
                updateMovieDiscussionUpdatedPostCommentRecyclerView(it)
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
            FrequencyFunctions.updateVoteColors(
                binding,
                this,
                isUpVoted,
                isDownVoted
            )
        } else {
            FrequencyFunctions.updateVoteColors(
                binding,
                this,
                isUpVoted = false,
                isDownVoted = false
            )
        }
        binding.answersQuantity.text = postDatabaseModel.commentsQuantity
        binding.textAnswersQuantity.visibility = when (postDatabaseModel.commentsQuantity.toInt()) {
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
        Glide.with(this).load(postDatabaseModel.user?.profilePicture)
            .error(R.drawable.pic_profile_picture).into(binding.profilePicture)

        binding.postDate.text = postDatabaseModel.timestamp
    }

    private fun initMovieDiscussionPostCommentRecyclerView() {
        movieDiscussionPostCommentAdapter = MovieDiscussionPostCommentAdapter(
            object : AdapterOnItemClickListenerWithView {
                override fun <T> onItemClickListener(view: View, item: T, position: Int) {
                    itemPositionPost = position
                    when (view.id) {
                        binding.buttonUpVote.id -> {
                            updateCommentVote(
                                Constants.UPDATE_TYPE.Upvote,
                                item as CommentAppearanceModel,
                            )
                        }

                        binding.buttonDownVote.id -> {
                            updateCommentVote(
                                Constants.UPDATE_TYPE.Downvote,
                                item as CommentAppearanceModel
                            )
                        }
                    }
                }
            }
        )
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.commentRecyclerView.adapter = movieDiscussionPostCommentAdapter
    }

    private fun updateMovieDiscussionPostCommentRecyclerView(commentList: List<CommentAppearanceModel>) {
        movieDiscussionPostCommentAdapter.getDiscussionPostComments(commentList)
    }

    private fun updateMovieDiscussionUpdatedPostCommentRecyclerView(
        updatedComment: CommentAppearanceModel,
    ) {
        itemPositionPost?.let {
            movieDiscussionPostCommentAdapter.getUpdatedComment(updatedComment, it)
        }
    }

    private fun saveMovieDiscussionPostId() {
        movieDiscussionPostViewModel.saveDataToSharedPreferences(
            Constants.LOCAL_STORAGE.sharedPreferencesPostIdKey, postAppearanceModel.postId
        )
    }

    private fun updateCommentVote(
        updateType: Constants.UPDATE_TYPE,
        commentAppearanceModel: CommentAppearanceModel,
    ) {
        movieDiscussionPostViewModel.updateCommentVote(updateType, commentAppearanceModel.commentId)
    }
}
