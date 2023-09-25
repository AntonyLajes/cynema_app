package com.nomargin.cynema.ui.fragment.home_fragment

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.bumptech.glide.Glide
import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.firebase.entity.UserProfileDataModel
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import com.nomargin.cynema.data.remote.retrofit.entity.MovieModel
import com.nomargin.cynema.databinding.FragmentHomeBinding
import com.nomargin.cynema.ui.adapter.recycler_view.FragmentHomeGenresAdapter
import com.nomargin.cynema.ui.adapter.recycler_view.MoviePosterAdapter
import com.nomargin.cynema.ui.adapter.view_pager.MainCarouselAdapter
import com.nomargin.cynema.util.Constants
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getHomeScreenData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observers() {
        homeViewModel.genres.observe(viewLifecycleOwner) { genreList ->
            initGenresRecyclerView(genreList)
            finishShimmerLayout()
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
        homeViewModel.upcomingMovies.observe(viewLifecycleOwner) { upcoming ->
            initUpcomingMoviesRecyclerView(upcoming)
        }
        homeViewModel.userProfileData.observe(viewLifecycleOwner) { userProfile ->
            userProfileHandler(userProfile)
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
            MainCarouselAdapter(movies.take(7), object : AdapterOnItemClickListener {
                override fun <T> onItemClickListener(item: T, position: Int) {
                    val movieModel = item as CarouselModel
                    homeViewModel.saveDataToSharedPreferences(
                        Constants.LOCAL_STORAGE.sharedPreferencesMovieIdKey,
                        movieModel.id.toString()
                    )
                    requireActivity()
                        .findNavController(
                            R.id.fragmentContainerView
                        )
                        .navigate(
                            HomeFragmentDirections.actionHomeFragmentToMovieFragment()
                        )
                }
            })
    }

    private fun initGenresRecyclerView(genres: List<GenreModel>) {
        binding.includesHomeFragmentCategories.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.includesHomeFragmentCategories.categoriesRecyclerView.adapter =
            FragmentHomeGenresAdapter(genres.take(10))
    }

    private fun initNowPlayingRecyclerView(movies: List<MovieModel>) {
        binding.includesHomeFragmentNowPlayingMovies.recyclerViewTitle.text =
            getString(R.string.now_playing)
        binding.includesHomeFragmentNowPlayingMovies.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.includesHomeFragmentNowPlayingMovies.recyclerView.adapter =
            MoviePosterAdapter(movies.take(7))
    }

    private fun initTopRatedMoviesRecyclerView(movies: List<MovieModel>) {
        binding.includesHomeFragmentTopRatedMovies.recyclerViewTitle.text =
            getString(R.string.top_rated)
        binding.includesHomeFragmentTopRatedMovies.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.includesHomeFragmentTopRatedMovies.recyclerView.adapter =
            MoviePosterAdapter(movies.take(7))
    }

    private fun initUpcomingMoviesRecyclerView(movies: List<MovieModel>) {
        binding.includesHomeFragmentUpcomingMovies.recyclerViewTitle.text =
            getString(R.string.upcoming)
        binding.includesHomeFragmentUpcomingMovies.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.includesHomeFragmentUpcomingMovies.recyclerView.adapter =
            MoviePosterAdapter(movies.take(7))
    }

    private fun userProfileHandler(userProfile: UserProfileDataModel?) {
        userProfile?.let {
            when {
                it.firstName.isNullOrEmpty() && it.profilePicture.isNullOrEmpty() -> {
                    binding.welcomeGreetings.text = buildString {
                        append(getString(R.string.welcome))
                        append("! ")
                        append(getString(R.string.hi_emoji))
                    }
                }

                it.firstName.isNullOrEmpty() && !it.profilePicture.isNullOrEmpty() -> {
                    binding.welcomeGreetings.text = buildString {
                        append(getString(R.string.welcome))
                        append("! ")
                        append(getString(R.string.hi_emoji))
                    }

                    Glide.with(this)
                        .load(it.profilePicture)
                        .error(R.drawable.pic_profile_picture)
                        .into(binding.profilePicture)
                }

                else -> {
                    binding.welcomeGreetings.text =
                        buildString {
                            append(getString(R.string.welcome))
                            append(", ")
                            append(userProfile.firstName)
                            append("! ")
                            append(getString(R.string.hi_emoji))
                        }

                    Glide.with(this)
                        .load(it.profilePicture)
                        .error(R.drawable.pic_profile_picture)
                        .into(binding.profilePicture)
                }
            }
        }
    }
}