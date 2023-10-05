package com.nomargin.cynema.ui.fragment.movie_discussion_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.databinding.FragmentMovieDiscussionBinding
import com.nomargin.cynema.databinding.ItemDiscussionPostBinding
import com.nomargin.cynema.ui.activity.movie_discussion_post_activity.MovieDiscussionPostActivity
import com.nomargin.cynema.ui.adapter.recycler_view.MovieDiscussionPostAdapter
import com.nomargin.cynema.ui.fragment.create_post_sheet_fragment.CreatePostBottomSheetFragment
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.extension.AdapterOnItemClickListenerWithView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDiscussionFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentMovieDiscussionBinding? = null
    private val binding: FragmentMovieDiscussionBinding get() = _binding!!
    private var _itemDiscussionPostBinding: ItemDiscussionPostBinding? = null
    private val itemDiscussionPostBinding: ItemDiscussionPostBinding? get() = _itemDiscussionPostBinding
    private val movieDiscussionViewModel: MovieDiscussionViewModel by viewModels()
    private lateinit var movieDiscussionPostAdapter: MovieDiscussionPostAdapter
    private var itemPositionPost: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDiscussionBinding.inflate(inflater)
        _itemDiscussionPostBinding = ItemDiscussionPostBinding.inflate(inflater)
        initMovieDiscussionPostRecyclerView()
        observers()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getMovieDiscussionPosts()
        initClicks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _itemDiscussionPostBinding = null
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.buttonCreatePost.id -> {
                initCreatePostBottomSheet()
            }

            binding.textNothingToShow.id -> {
                initCreatePostBottomSheet()
            }
        }
    }

    private fun initCreatePostBottomSheet() {
        val createPostBottomSheetDialogFragment = CreatePostBottomSheetFragment()
        createPostBottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            "createPostBottomSheetDialogFragment"
        )
    }

    private fun initMovieDiscussionPostRecyclerView() {
        movieDiscussionPostAdapter =
            MovieDiscussionPostAdapter(object : AdapterOnItemClickListenerWithView {
                override fun <T> onItemClickListener(view: View, item: T, position: Int) {
                    itemPositionPost = position
                    when (view.id) {
                        itemDiscussionPostBinding!!.post.id -> {
                            postClicksListener(item as PostAppearanceModel)
                        }

                        itemDiscussionPostBinding!!.buttonUpVote.id -> {
                            updatePostVote(
                                Constants.UPDATE_TYPE.Upvote,
                                item as PostAppearanceModel
                            )
                        }

                        itemDiscussionPostBinding!!.buttonDownVote.id -> {
                            updatePostVote(
                                Constants.UPDATE_TYPE.Downvote,
                                item as PostAppearanceModel
                            )
                        }
                    }
                }
            })
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.scrollToPositionWithOffset(0, 0)
        binding.movieDiscussionPostRecyclerView.layoutManager =
            layoutManager
        binding.movieDiscussionPostRecyclerView.adapter = movieDiscussionPostAdapter
    }

    private fun initClicks() {
        binding.buttonCreatePost.setOnClickListener(this)
        binding.textNothingToShow.setOnClickListener(this)
        binding.movieDiscussionPostSwipeToRefresh.setOnRefreshListener {
            getMovieDiscussionPosts()
            binding.movieDiscussionPostSwipeToRefresh.isRefreshing = false
        }
    }

    private fun observers() {
        movieDiscussionViewModel.getPosts.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                if (it.isEmpty()) {
                    binding.textNothingToShow.visibility = View.VISIBLE
                    binding.movieDiscussionPostSwipeToRefresh.visibility = View.GONE
                    binding.loadingProgressBar.visibility = View.GONE
                } else {
                    binding.movieDiscussionPostSwipeToRefresh.visibility = View.VISIBLE
                    binding.textNothingToShow.visibility = View.GONE
                    binding.loadingProgressBar.visibility = View.GONE
                    updateMovieDiscussionPostRecyclerView(it)
                }
            }
        }
        movieDiscussionViewModel.getUpdatedPost.observe(viewLifecycleOwner) { updatedPost ->
            updatedPost?.let {
                updateMovieDiscussionUpdatedPostRecyclerView(it)
            }
        }
    }

    private fun updateMovieDiscussionPostRecyclerView(postDatabaseModels: List<PostAppearanceModel>) {
        movieDiscussionPostAdapter.getDiscussionPosts(postDatabaseModels)
    }

    private fun updateMovieDiscussionUpdatedPostRecyclerView(
        updatedPost: PostAppearanceModel
    ) {
        itemPositionPost?.let {
            movieDiscussionPostAdapter.getUpdatedPost(updatedPost, it)
        }
    }

    private fun getMovieDiscussionPosts() {
        movieDiscussionViewModel.getPosts()
    }

    private fun postClicksListener(item: PostAppearanceModel) {
        val intent = Intent(requireContext(), MovieDiscussionPostActivity::class.java)
        val bundle = Bundle()
        bundle.putString(Constants.BUNDLE_KEYS.MovieDiscussionPostId.toString(), item.postId)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun updatePostVote(
        updateType: Constants.UPDATE_TYPE,
        postAppearanceModel: PostAppearanceModel
    ) {
        movieDiscussionViewModel.updatePostVote(updateType, postAppearanceModel.postId)
    }

}