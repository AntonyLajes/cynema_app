package com.nomargin.cynema.ui.fragment.favorites_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.MovieSearchedDetailsModel
import com.nomargin.cynema.databinding.FragmentFavoritesBinding
import com.nomargin.cynema.ui.adapter.recycler_view.MovieSearchPosterAdapter
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.FrequencyFunctions
import com.nomargin.cynema.util.FrequencyFunctions.makeToast
import com.nomargin.cynema.util.Status
import com.nomargin.cynema.util.extension.AdapterOnItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding get() = _binding!!
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var movieSearchPosterAdapter: MovieSearchPosterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observers()
        getFavoriteMovies()
        swipeToRefresh()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun swipeToRefresh(){
        binding.favoriteMoviesSwipeToRefresh.setOnRefreshListener {
            getFavoriteMovies()
            binding.favoriteMoviesSwipeToRefresh.isRefreshing = false
        }
    }

    private fun observers() {
        favoritesViewModel.moviesAddedInFavoriteResult.observe(viewLifecycleOwner) { moviesAddedInFavoriteResult ->
            if (moviesAddedInFavoriteResult.status == Status.SUCCESS) {
                if (moviesAddedInFavoriteResult.statusModel?.isValid == true) {
                    binding.textNothingToShow.visibility = if(moviesAddedInFavoriteResult.data.isNullOrEmpty()){
                        initMovieSearchPosterAdapter(emptyList())
                        View.VISIBLE
                    }else{
                        initMovieSearchPosterAdapter(moviesAddedInFavoriteResult.data)
                        View.GONE
                    }
                } else {
                    makeToast(
                        requireContext(),
                        moviesAddedInFavoriteResult.statusModel?.message ?: R.string.unknown_error
                    )
                }
            } else {
                makeToast(
                    requireContext(),
                    moviesAddedInFavoriteResult.statusModel?.message ?: R.string.unknown_error
                )
            }
            binding.loadingProgressBar.visibility = View.GONE
        }
    }

    private fun getFavoriteMovies() {
        favoritesViewModel.getFavoriteMovies()
    }

    private fun initMovieSearchPosterAdapter(movieDetailsByQueryModel: List<MovieSearchedDetailsModel>) {
        movieSearchPosterAdapter =
            MovieSearchPosterAdapter(movieDetailsByQueryModel, object : AdapterOnItemClickListener {
                override fun <T> onItemClickListener(item: T, position: Int) {
                    FrequencyFunctions.navigateToMovieDetails(
                        Constants.CLASS_TYPE.favoriteMovieSearchedDetailsModel,
                        item,
                        requireActivity(),
                        favoritesViewModel
                    )
                }
            })
        binding.favoriteMoviesRecyclerView.adapter = movieSearchPosterAdapter
        binding.favoriteMoviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}