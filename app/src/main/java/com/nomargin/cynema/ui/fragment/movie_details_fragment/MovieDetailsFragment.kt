package com.nomargin.cynema.ui.fragment.movie_details_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.nomargin.cynema.data.remote.retrofit.entity.MovieDetailsModel
import com.nomargin.cynema.databinding.FragmentMovieDetailsBinding
import com.nomargin.cynema.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding: FragmentMovieDetailsBinding get() = _binding!!
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieDetailsViewModel.getDataFromSharedPreferences(
            Constants.LOCAL_STORAGE.sharedPreferencesMovieIdKey,
            ""
        )
        observers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observers() {
        movieDetailsViewModel.movieDetails.observe(viewLifecycleOwner){movieDetails ->
            fieldsHandler(movieDetails)
        }
    }

    private fun fieldsHandler(movieDetails: MovieDetailsModel) {
        

    }

}