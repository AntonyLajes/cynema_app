package com.nomargin.cynema.ui.activity.movie_discussion_post_activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.CommentAppearanceModel
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.databinding.ActivityMovieDiscussionPostBinding
import com.nomargin.cynema.ui.adapter.recycler_view.MovieDiscussionPostCommentAdapter
import com.nomargin.cynema.ui.fragment.create_comment_post_sheet_fragment.CreateCommentPostBottomSheetFragment
import com.nomargin.cynema.ui.fragment.handle_post_bottom_sheet_fragment.HandlePostBottomSheetFragment
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.FrequencyFunctions
import com.nomargin.cynema.util.extension.AdapterOnItemClickListenerWithView
import com.nomargin.cynema.util.extension.OnSaveClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDiscussionPostActivity : AppCompatActivity(), View.OnClickListener, OnSaveClickListener {

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
    }

    override fun onResume() {
        super.onResume()
        observers()
        initMovieDiscussionPostCommentRecyclerView()
        getMovieDiscussionPostId()
        initClicks()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                bundle.putString(
                    Constants.BUNDLE_KEYS.MovieDiscussionPostId.name,
                    postAppearanceModel.postId
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

            binding.menuMore.id -> {
                showMenu(view, R.menu.menu_post)
            }
        }
    }

    override fun onSaveClicked() {
        getPostDetails()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showMenu(view: View, @MenuRes menuRes: Int) {
        val popUp = PopupMenu(applicationContext, view)
        popUp.menuInflater.inflate(menuRes, popUp.menu)
        popUp.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_edit_post -> {
                    showEditPostBottomSheet()
                    true
                }

                R.id.menu_delete_post -> {

                    true
                }

                else -> {
                    false
                }
            }
        }
        popUp.show()
    }

    private fun initClicks() {
        binding.addComment.setOnClickListener(this)
        binding.buttonUpVote.setOnClickListener(this)
        binding.buttonDownVote.setOnClickListener(this)
        binding.menuMore.setOnClickListener(this)
        binding.swipeToRefresh.setOnRefreshListener {
            getPostDetails()
        }
    }

    private fun getMovieDiscussionPostId() {
        val bundle = intent.extras
        if (bundle != null) {
            movieDiscussionPostId = bundle.getString(
                Constants.BUNDLE_KEYS.MovieDiscussionPostId.toString()
            )
            getPostDetails()
        }
    }

    private fun getPostDetails() {
        movieDiscussionPostViewModel.getDiscussionPostById(movieDiscussionPostId)
        movieDiscussionPostViewModel.getAllComments(movieDiscussionPostId)
    }

    private fun observers() {
        movieDiscussionPostViewModel.post.observe(this) { post ->
            binding.swipeToRefresh.isRefreshing = false
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
                updateMovieDiscussionPostCommentRecyclerView(it)
            }
        }
        movieDiscussionPostViewModel.getUpdatedComment.observe(this) { updatedComment ->
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

    private fun showEditPostBottomSheet() {
        val bundle = Bundle()
        bundle.putString(
            Constants.BUNDLE_KEYS.MovieDiscussionPostId.name,
            postAppearanceModel.postId
        )
        bundle.putString(
            Constants.BUNDLE_KEYS.MovieDiscussionHandlerType.name,
            Constants.BOTTOM_SHEET_TYPE.MovieDiscussionHandlerEdit.name
        )
        val editPostBottomSheetFragment = HandlePostBottomSheetFragment()
        editPostBottomSheetFragment.setOnSaveClickListener(this)
        editPostBottomSheetFragment.arguments = bundle
        editPostBottomSheetFragment.show(
            this.supportFragmentManager, "editPostBottomSheetFragment"
        )
    }
}
