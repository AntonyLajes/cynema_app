package com.nomargin.cynema.ui.fragment.create_comment_post_sheet_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nomargin.cynema.R
import com.nomargin.cynema.databinding.FragmentCreateCommentPostBottomSheetBinding
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCommentPostBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: FragmentCreateCommentPostBottomSheetBinding? = null
    private val binding: FragmentCreateCommentPostBottomSheetBinding get() = _binding!!
    private val createCommentPostBottomSheetViewModel: CreateCommentPostBottomSheetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateCommentPostBottomSheetBinding.inflate(inflater, container, false)
        observers()
        fieldsHandler()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.buttonComment.id -> {
                createCommentPostBottomSheetViewModel.publishComment(
                    binding.commentBodyInput.text.toString(),
                    binding.chipSpoiler.isChecked
                )
            }
        }
    }

    private fun initClicks() {
        binding.buttonComment.setOnClickListener(this)
    }

    private fun observers() {
        createCommentPostBottomSheetViewModel.createCommentStatus.observe(this) { createCommentStatus ->
            if (createCommentStatus.status == Status.SUCCESS) {
                Log.d("createCommentStatus", "observers: ${createCommentStatus.data}")
            } else {
                Log.d("createCommentStatus", "observers: ${createCommentStatus.statusModel}")
            }
        }
    }

    private fun fieldsHandler() {
        binding.descStart.text = buildString {
            append(requireActivity().getString(R.string.commenting_on))
            append(" ")
        }
        binding.parentName.text = buildString {
            append(
                arguments?.getString(
                    Constants.BUNDLE_KEYS.MovieDiscussionPostOwnerName.name,
                    "user's"
                )
            )
            append("'s")
            append(" ")
        }
        binding.descEnd.text = buildString {
            append(requireActivity().getString(R.string.post))
            append(".")
        }
    }
}