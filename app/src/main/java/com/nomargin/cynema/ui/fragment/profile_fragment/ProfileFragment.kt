package com.nomargin.cynema.ui.fragment.profile_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.firebase.entity.UserProfileDataModel
import com.nomargin.cynema.databinding.FragmentProfileBinding
import com.nomargin.cynema.ui.activity.create_profile_activity.CreateProfileActivity
import com.nomargin.cynema.ui.adapter.view_pager.ViewPagerAdapter
import com.nomargin.cynema.ui.fragment.post_list_fragment.PostListFragment
import com.nomargin.cynema.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    private val viewPagerAdapter: ViewPagerAdapter by lazy { ViewPagerAdapter(requireActivity()) }
    private val tabLayout: TabLayout by lazy { binding.tabLayout }
    private val viewPager: ViewPager2 by lazy { binding.viewPager }
    private val postListFragment = PostListFragment()
    private var userId: String = ""
    private lateinit var userData: UserProfileDataModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        observers()
        getUserData()
        initClicks()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.buttonEditProfile.id -> {
                openEditProfileScreen()
            }
        }
    }

    private fun openEditProfileScreen() {
        val intent = Intent(requireContext(), CreateProfileActivity::class.java)
        val bundle = Bundle()
        bundle.putString(
            Constants.BUNDLE_KEYS.UserId.name,
            userId
        )
        bundle.putString(
            Constants.BUNDLE_KEYS.UserProfileHandlerType.name,
            Constants.UPDATE_TYPE.UpdateProfile.name
        )
        bundle.putParcelable(
            Constants.BUNDLE_KEYS.UserData.name,
            userData
        )
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun initClicks() {
        binding.buttonEditProfile.setOnClickListener(this)
    }

    private fun observers() {
        profileViewModel.userData.observe(viewLifecycleOwner) { data ->
            data?.let {
                userId = it.id ?: ""
                userData = it
                fieldsHandler(it)
                val bundle: Bundle = Bundle()
                    .apply {
                        putStringArray(
                            Constants.BUNDLE_KEYS.UserPostsCreated.name,
                            it.posts?.toTypedArray()
                        )
                    }
                postListFragment.arguments = bundle
                viewPagerInflater()
            }
        }
    }

    private fun viewPagerInflater() {
        viewPager.adapter = viewPagerAdapter
        viewPagerAdapter.setFragment(postListFragment, getString(R.string.posts))
        viewPager.offscreenPageLimit = viewPagerAdapter.itemCount
        val mediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewPagerAdapter.getTitle(position)
        }
        mediator.attach()
    }

    private fun getUserData() {
        profileViewModel.getUserData()
    }

    private fun fieldsHandler(userData: UserProfileDataModel) {
        binding.userName.text = buildString {
            append(userData.firstName)
            append(" ")
            append(userData.lastName)
        }
        binding.userUsername.text = buildString {
            append("@")
            append(userData.username)
        }
        Glide.with(this)
            .load(userData.profilePicture)
            .error(R.drawable.pic_profile_picture)
            .into(binding.userProfilePicture)
        binding.userStatus.visibility = if (userData.biography.isNullOrEmpty()) {
            View.GONE
        } else {
            binding.userStatus.text = userData.biography
            View.VISIBLE
        }
    }

}