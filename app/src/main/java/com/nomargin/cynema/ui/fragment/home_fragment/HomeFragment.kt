package com.nomargin.cynema.ui.fragment.home_fragment

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import com.nomargin.cynema.data.remote.retrofit.entity.MovieModel
import com.nomargin.cynema.databinding.FragmentHomeBinding
import com.nomargin.cynema.ui.adapter.recycler_view.FragmentHomeGenresAdapter
import com.nomargin.cynema.ui.adapter.recycler_view.MoviePosterAdapter
import com.nomargin.cynema.ui.adapter.view_pager.MainCarouselAdapter
import com.nomargin.cynema.util.extension.AdapterOnItemClickListener
import com.nomargin.cynema.util.model.CarouselModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.includeShimmerLayout.shimmerLayout.startShimmer()
        observers()
        homeViewModel.getHomePageData()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observers() {
        homeViewModel.genres.observe(viewLifecycleOwner) { genreList ->
            finishShimmerLayout()
            initGenresRecyclerView(genreList)
        }
        homeViewModel.movieModelToCarouselModel.observe(viewLifecycleOwner) { movieList ->
            initCarousel(movieList)
        }
        homeViewModel.nowPlayingMovies.observe(viewLifecycleOwner) { nowPlaying ->
            initNowPlayingRecyclerView(nowPlaying)
        }
        homeViewModel.topRatedMovies.observe(viewLifecycleOwner) { topRated ->
            initTopRatedMoviesRecyclerView(topRated)
        }
        homeViewModel.upComingMovies.observe(viewLifecycleOwner) { upComing ->
            initUpcomingMoviesRecyclerView(upComing)
        }
    }

    private fun finishShimmerLayout() {
        binding.includeShimmerLayout.shimmerLayout.stopShimmer()
        binding.includeShimmerLayout.shimmerLayout.visibility = View.GONE
        binding.mainView.visibility = View.VISIBLE
    }

    private fun initCarousel(movies: List<CarouselModel>) {
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        binding.viewPager.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(compositePageTransformer)
        }

        binding.viewPager.adapter =
            MainCarouselAdapter(movies, object : AdapterOnItemClickListener {
                override fun <T> onItemClickListener(item: T, position: Int) {

                }
            })
    }

    private fun initGenresRecyclerView(genres: List<GenreModel>) {
        val fragmentHomeGenresAdapter = FragmentHomeGenresAdapter(genres)

        binding.includesHomeFragmentCategories.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.includesHomeFragmentCategories.categoriesRecyclerView.adapter =
            fragmentHomeGenresAdapter
    }

    private fun initNowPlayingRecyclerView(movies: List<MovieModel>) {
        binding.includesHomeFragmentNowPlayingMovies.recyclerViewTitle.text =
            getString(R.string.now_playing)
        binding.includesHomeFragmentNowPlayingMovies.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.includesHomeFragmentNowPlayingMovies.recyclerView.adapter = MoviePosterAdapter(movies)
    }

    private fun initTopRatedMoviesRecyclerView(movies: List<MovieModel>) {
        binding.includesHomeFragmentTopRatedMovies.recyclerViewTitle.text =
            getString(R.string.top_rated)
        binding.includesHomeFragmentTopRatedMovies.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.includesHomeFragmentTopRatedMovies.recyclerView.adapter =
            MoviePosterAdapter(movies)
    }

    private fun initUpcomingMoviesRecyclerView(movies: List<MovieModel>) {
        binding.includesHomeFragmentUpcomingMovies.recyclerViewTitle.text =
            getString(R.string.upcoming)
        binding.includesHomeFragmentUpcomingMovies.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.includesHomeFragmentUpcomingMovies.recyclerView.adapter =
            MoviePosterAdapter(movies)
    }
}