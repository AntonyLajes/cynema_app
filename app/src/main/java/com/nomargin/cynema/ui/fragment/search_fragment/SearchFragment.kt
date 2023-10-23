package com.nomargin.cynema.ui.fragment.search_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nomargin.cynema.data.local.entity.MovieSearchedAppearanceModel
import com.nomargin.cynema.databinding.FragmentSearchBinding
import com.nomargin.cynema.ui.adapter.recycler_view.MovieSearchPosterAdapter
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.FrequencyFunctions
import com.nomargin.cynema.util.extension.AdapterOnItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var movieSearchPosterAdapter: MovieSearchPosterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        searchViewModel.getPopularMovies()
        searchHandler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observers() {
        searchViewModel.movieSearchByQueryResult.observe(viewLifecycleOwner) { movieSearchResult ->
            updateMovieSearchPosterAdapter(movieSearchResult)
            fieldHandlers(movieSearchResult.totalResults)
        }
        searchViewModel.popularMoviesResult.observe(viewLifecycleOwner) { popularMoviesResult ->
            initMovieSearchPosterAdapter(popularMoviesResult)
            fieldHandlers(popularMoviesResult.totalResults)
        }
    }

    private fun updateMovieSearchPosterAdapter(movieSearchResult: MovieSearchedAppearanceModel?) {
        movieSearchResult?.let {
            movieSearchPosterAdapter.setSearchedMovies(it)
        }
    }

    private fun initMovieSearchPosterAdapter(movieDetailsByQueryModel: MovieSearchedAppearanceModel) {
        movieSearchPosterAdapter =
            MovieSearchPosterAdapter(movieDetailsByQueryModel, object : AdapterOnItemClickListener {
                override fun <T> onItemClickListener(item: T, position: Int) {
                    FrequencyFunctions.navigateToMovieDetails(
                        Constants.CLASS_TYPE.movieSearchedDetailsModel,
                        item,
                        requireActivity(),
                        searchViewModel
                    )
                }
            })
        binding.searchRecyclerView.adapter = movieSearchPosterAdapter
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun fieldHandlers(searchLength: Int) {
        binding.textNothingFind.visibility = if (searchLength <= 0) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun searchHandler() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    searchViewModel.getPopularMovies()
                } else {
                    searchViewModel.doSearch(newText)
                }
                return true
            }
        })
    }

}