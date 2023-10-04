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
import com.nomargin.cynema.ui.activity.movie_discussion_post_activity.MovieDiscussionPostActivity
import com.nomargin.cynema.ui.adapter.recycler_view.MovieDiscussionPostAdapter
import com.nomargin.cynema.ui.fragment.create_post_sheet_fragment.CreatePostBottomSheetFragment
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.extension.AdapterOnItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDiscussionFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentMovieDiscussionBinding? = null
    private val binding: FragmentMovieDiscussionBinding get() = _binding!!
    private val movieDiscussionViewModel: MovieDiscussionViewModel by viewModels()
    private lateinit var movieDiscussionPostAdapter: MovieDiscussionPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDiscussionBinding.inflate(inflater)
        initMovieDiscussionPostRecyclerView()
        observers()
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
        movieDiscussionPostAdapter =
            MovieDiscussionPostAdapter(object : AdapterOnItemClickListener {
                override fun <T> onItemClickListener(item: T, position: Int) {
                    postClicksListener(item as PostAppearanceModel)
                }
            })
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

    private fun postClicksListener(item: PostAppearanceModel) {
        val intent = Intent(requireContext(), MovieDiscussionPostActivity::class.java)
        val bundle = Bundle()
        bundle.putString(Constants.BUNDLE_KEYS.MovieDiscussionPostId.toString(), item.postId)
        intent.putExtras(bundle)
        startActivity(intent)
    }

}