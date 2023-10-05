package com.nomargin.cynema.ui.fragment.create_comment_post_sheet_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nomargin.cynema.databinding.FragmentCreateCommentPostBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCommentPostBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCreateCommentPostBottomSheetBinding? = null
    private val binding: FragmentCreateCommentPostBottomSheetBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateCommentPostBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}