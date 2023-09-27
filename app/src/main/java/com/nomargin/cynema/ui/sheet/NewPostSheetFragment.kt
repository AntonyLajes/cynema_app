package com.nomargin.cynema.ui.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nomargin.cynema.databinding.FragmentNewPostSheetBinding

class NewPostSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentNewPostSheetBinding? = null
    private val binding: FragmentNewPostSheetBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPostSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}