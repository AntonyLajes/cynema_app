package com.nomargin.cynema.ui.fragment.movie_details_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import com.nomargin.cynema.data.remote.retrofit.entity.MovieDetailsModel
import com.nomargin.cynema.databinding.FragmentMovieDetailsBinding
import com.nomargin.cynema.ui.adapter.recycler_view.FragmentMovieDetailsGenresAdapter
import com.nomargin.cynema.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

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
        binding.includeShimmerMovieDetailsFragmentLayout.movieDetailsFragmentShimmerLayout.startShimmer()
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
        movieDetailsViewModel.movieDetails.observe(viewLifecycleOwner) { movieDetails ->
            fieldsHandler(movieDetails)
            finishShimmerLayout()
        }
    }

    private fun finishShimmerLayout() {
        binding.includeShimmerMovieDetailsFragmentLayout.movieDetailsFragmentShimmerLayout.stopShimmer()
        binding.includeShimmerMovieDetailsFragmentLayout.movieDetailsFragmentShimmerLayout.visibility = View.GONE
        binding.fragmentMovieDetailsMainView.visibility = View.VISIBLE
    }

    private fun fieldsHandler(movieDetails: MovieDetailsModel) {
        Glide.with(requireContext())
            .load(Constants.TMDB_PATH_URLs.posterPathUrl + movieDetails.backdropPath)
            .centerCrop()
            .into(binding.moviePoster)
        binding.movieName.text = movieDetails.originalTitle
        val decimalFormat = DecimalFormat("#.#")
        val movieRateRounded = decimalFormat.format(movieDetails.voteAverage)
        binding.movieRate.text = buildString {
            append(movieRateRounded)
            append("/10")
        }
        binding.movieReleaseDate.text = movieDetails.releaseDate
        binding.movieSynopsys.text = movieDetails.overview
        initGenresRecyclerView(movieDetails.genres)
    }

    private fun initGenresRecyclerView(genres: List<GenreModel>) {
        binding.movieGenres.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.movieGenres.adapter =
            FragmentMovieDetailsGenresAdapter(genres)
    }

}