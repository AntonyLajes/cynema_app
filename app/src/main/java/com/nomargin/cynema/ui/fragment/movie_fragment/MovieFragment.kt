package com.nomargin.cynema.ui.fragment.movie_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nomargin.cynema.R
import com.nomargin.cynema.databinding.FragmentMovieBinding
import com.nomargin.cynema.ui.adapter.view_pager.ViewPagerAdapter
import com.nomargin.cynema.ui.fragment.movie_details_fragment.MovieDetailsFragment
import com.nomargin.cynema.ui.fragment.movie_discussion_fragment.MovieDiscussionFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding: FragmentMovieBinding get() = _binding!!
    private val tabLayout: TabLayout by lazy { binding.tabLayout }
    private val viewPager: ViewPager2 by lazy { binding.viewPager }
    private val viewPagerAdapter: ViewPagerAdapter by lazy { ViewPagerAdapter(requireActivity()) }
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater)
        viewPagerInflater()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireArguments().clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
    }

    private fun observers() {
    }

    private fun viewPagerInflater() {
        viewPager.adapter = viewPagerAdapter
        viewPagerAdapter.setFragment(MovieDetailsFragment(), getString(R.string.details))
        viewPagerAdapter.setFragment(MovieDiscussionFragment(), getString(R.string.discussion))
        viewPager.offscreenPageLimit = viewPagerAdapter.itemCount
        val mediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewPagerAdapter.getTitle(position)
            when (position) {
                0 -> {
                    tab.icon =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_details)
                }

                1 -> {
                    tab.icon =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_discussion)
                }
            }
        }
        mediator.attach()
    }

}