package com.nomargin.cynema.ui.fragment.movie_discussion_fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nomargin.cynema.R

class MovieDiscussionFragment : Fragment() {

    companion object {
        fun newInstance() = MovieDiscussionFragment()
    }

    private lateinit var viewModel: MovieDiscussionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_discussion, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MovieDiscussionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}