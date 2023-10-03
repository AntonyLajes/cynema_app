package com.nomargin.cynema.ui.fragment.movie_discussion_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.databinding.FragmentMovieDiscussionBinding
import com.nomargin.cynema.ui.adapter.recycler_view.MovieDiscussionPostAdapter
import com.nomargin.cynema.ui.fragment.create_post_sheet_fragment.CreatePostBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDiscussionFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentMovieDiscussionBinding? = null
    private val binding: FragmentMovieDiscussionBinding get() = _binding!!
    private val movieDiscussionViewModel: MovieDiscussionViewModel by viewModels()
    private val movieDiscussionPostAdapter: MovieDiscussionPostAdapter by lazy { MovieDiscussionPostAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDiscussionBinding.inflate(inflater)
        observers()
        initMovieDiscussionPostRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMovieDiscussionPosts()
        initClicks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.buttonCreatePost.id -> {
                val createPostBottomSheetDialogFragment = CreatePostBottomSheetFragment()
                createPostBottomSheetDialogFragment.show(
                    requireActivity().supportFragmentManager,
                    "createPostBottomSheetDialogFragment"
                )
            }
        }
    }

    private fun initMovieDiscussionPostRecyclerView() {
        binding.movieDiscussionPostRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.movieDiscussionPostRecyclerView.adapter = movieDiscussionPostAdapter
    }

    private fun initClicks() {
        binding.buttonCreatePost.setOnClickListener(this)
        binding.movieDiscussionPostSwipeToRefresh.setOnRefreshListener {
            getMovieDiscussionPosts()
            binding.movieDiscussionPostSwipeToRefresh.isRefreshing = false
        }
    }

    private fun observers() {
        movieDiscussionViewModel.getPosts.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                updateMovieDiscussionPostRecyclerView(it)
            }
        }
    }

    private fun updateMovieDiscussionPostRecyclerView(postDatabaseModels: List<PostAppearanceModel>) {
        movieDiscussionPostAdapter.getDiscussionPosts(postDatabaseModels)
    }

    private fun getMovieDiscussionPosts() {
        movieDiscussionViewModel.getPosts()
    }

}