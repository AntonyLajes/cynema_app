package com.nomargin.cynema.ui.fragment.post_list_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nomargin.cynema.databinding.FragmentPostListBinding
import com.nomargin.cynema.ui.fragment.profile_fragment.ProfileViewModel
import com.nomargin.cynema.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostListFragment : Fragment() {

    private var _binding: FragmentPostListBinding? = null
    private val binding: FragmentPostListBinding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    private val userPostsList = arguments?.getStringArray(Constants.BUNDLE_KEYS.UserPostsCreated.name)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPostListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserPostList()
    }

    private fun getUserPostList(){
        Log.d("getUserPostList", "getUserPostList: $userPostsList")
    }

}