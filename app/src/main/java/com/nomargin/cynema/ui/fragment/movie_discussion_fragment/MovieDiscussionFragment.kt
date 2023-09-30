package com.nomargin.cynema.ui.fragment.movie_discussion_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nomargin.cynema.databinding.FragmentMovieDiscussionBinding
import com.nomargin.cynema.ui.adapter.recycler_view.MovieDiscussionPostAdapter

class MovieDiscussionFragment : Fragment() {

    private var _binding: FragmentMovieDiscussionBinding? = null
    private val binding: FragmentMovieDiscussionBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDiscussionBinding.inflate(inflater)
        initMovieDiscussionPostRecyclerView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initMovieDiscussionPostRecyclerView(){
        binding.movieDiscussionPostRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.movieDiscussionPostRecyclerView.adapter = MovieDiscussionPostAdapter()
    }

}