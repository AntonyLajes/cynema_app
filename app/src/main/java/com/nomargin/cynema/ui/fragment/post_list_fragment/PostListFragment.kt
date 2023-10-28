package com.nomargin.cynema.ui.fragment.post_list_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.databinding.FragmentPostListBinding
import com.nomargin.cynema.databinding.ItemDiscussionPostBinding
import com.nomargin.cynema.ui.activity.movie_discussion_post_activity.MovieDiscussionPostActivity
import com.nomargin.cynema.ui.adapter.recycler_view.MovieDiscussionPostAdapter
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.extension.AdapterOnItemClickListenerWithView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostListFragment : Fragment() {

    private var _binding: FragmentPostListBinding? = null
    private val binding: FragmentPostListBinding get() = _binding!!
    private val postListViewModel: PostListViewModel by viewModels()
    private var _itemDiscussionPostBinding: ItemDiscussionPostBinding? = null
    private val itemDiscussionPostBinding: ItemDiscussionPostBinding? get() = _itemDiscussionPostBinding
    private lateinit var movieDiscussionPostAdapter: MovieDiscussionPostAdapter
    private var itemPositionPost: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPostListBinding.inflate(inflater)
        _itemDiscussionPostBinding = ItemDiscussionPostBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _itemDiscussionPostBinding = null
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        getUserPostList()
    }

    private fun observers() {
        postListViewModel.userPost.observe(viewLifecycleOwner) { userPosts ->
            if (userPosts.isEmpty()) {

            } else {
                initMovieDiscussionPostAdapter(userPosts)
            }
        }
        postListViewModel.getUpdatedPost.observe(viewLifecycleOwner) { updatedPost ->
            updatedPost?.let {
                updateMovieDiscussionUpdatedPostRecyclerView(it)
            }
        }
    }

    private fun getUserPostList() {
        val userPostsList = arguments?.getStringArray(Constants.BUNDLE_KEYS.UserPostsCreated.name)
        binding.textNothingToShow.visibility = if (userPostsList.isNullOrEmpty()) {
            View.VISIBLE
        } else {
            postListViewModel.getUserPosts(userPostsList.toList())
            View.GONE
        }
    }

    private fun updateMovieDiscussionUpdatedPostRecyclerView(
        updatedPost: PostAppearanceModel,
    ) {
        itemPositionPost?.let {
            movieDiscussionPostAdapter.getUpdatedPost(updatedPost, it)
        }
    }

    private fun initMovieDiscussionPostAdapter(userPosts: List<PostAppearanceModel>) {
        movieDiscussionPostAdapter = MovieDiscussionPostAdapter(object :
            AdapterOnItemClickListenerWithView {
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
        binding.postListRecyclerView.adapter = movieDiscussionPostAdapter
        binding.postListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        movieDiscussionPostAdapter.getDiscussionPosts(userPosts)
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
        postAppearanceModel: PostAppearanceModel,
    ) {
        postListViewModel.updatePostVote(updateType, postAppearanceModel.postId)
    }

}