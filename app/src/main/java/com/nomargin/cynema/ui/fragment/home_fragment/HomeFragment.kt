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
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import com.nomargin.cynema.data.remote.retrofit.entity.MovieModel
import com.nomargin.cynema.databinding.FragmentHomeBinding
import com.nomargin.cynema.ui.adapter.recycler_view.FragmentHomeGenresAdapter
import com.nomargin.cynema.ui.adapter.view_pager.MainCarouselAdapter
import com.nomargin.cynema.util.extension.AdapterOnItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getGenres()
        homeViewModel.getPopularMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observers(){
        homeViewModel.genres.observe(viewLifecycleOwner){genreList ->
            finishShimmerLayout()
            initGenresRecyclerView(genreList)
        }
        homeViewModel.popularMovies.observe(viewLifecycleOwner){moviesResult ->
            initCarousel(moviesResult.results)
        }
    }

    private fun finishShimmerLayout() {
        binding.includeShimmerLayout.shimmerLayout.stopShimmer()
        binding.includeShimmerLayout.shimmerLayout.visibility = View.GONE
        binding.mainView.visibility = View.VISIBLE
    }

    private fun initCarousel(movies: List<MovieModel>) {
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        binding.viewPager.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(compositePageTransformer)
        }

        binding.viewPager.adapter = MainCarouselAdapter(movies, object : AdapterOnItemClickListener{
            override fun <T> onItemClickListener(item: T, position: Int) {
                val movie = item as MovieModel
            }
        })

    }

    private fun initGenresRecyclerView(genres: List<GenreModel>){

        val fragmentHomeGenresAdapter = FragmentHomeGenresAdapter(genres)

        binding.includesHomeFragmentCategories.categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.includesHomeFragmentCategories.categoriesRecyclerView.adapter = fragmentHomeGenresAdapter
    }
/*
    private fun initLatestMoviesRecyclerView(){
        val movieList = arrayListOf(
            MovieModel(R.drawable.rocket_1, "Rocket 1"),
            MovieModel(R.drawable.rocket_2, "Rocket 2"),
            MovieModel(R.drawable.rocket_3, "Rocket 3"),
            MovieModel(R.drawable.rocket_4, "Rocket 4"),
            MovieModel(R.drawable.rocket_5, "Rocket 5"),
        )

        val fragmentHomeLatestMoviesAdapter = MoviePosterAdapter(movieList)

        binding.includesHomeFragmentMostWatchedMovies.recyclerViewTitle.text = getString(R.string.latest_movies)
        binding.includesHomeFragmentLatestMovies.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.includesHomeFragmentLatestMovies.recyclerView.adapter = fragmentHomeLatestMoviesAdapter
    }

    private fun initLatestMostWatchedMoviesRecyclerView(){
        val movieList = arrayListOf(
            MovieModel(R.drawable.rocket_1, "Rocket 1"),
            MovieModel(R.drawable.rocket_2, "Rocket 2"),
            MovieModel(R.drawable.rocket_3, "Rocket 3"),
            MovieModel(R.drawable.rocket_4, "Rocket 4"),
            MovieModel(R.drawable.rocket_5, "Rocket 5"),
        )

        val fragmentHomeMostWatchedMoviesAdapter = MoviePosterAdapter(movieList)

        binding.includesHomeFragmentMostWatchedMovies.recyclerViewTitle.text = getString(R.string.most_watched)
        binding.includesHomeFragmentMostWatchedMovies.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.includesHomeFragmentMostWatchedMovies.recyclerView.adapter = fragmentHomeMostWatchedMoviesAdapter
    }
*/
}