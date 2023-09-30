package com.nomargin.cynema.ui.fragment.create_post_sheet_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.nomargin.cynema.R
import com.nomargin.cynema.databinding.FragmentCreatePostBottomSheetBinding

class CreatePostBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: FragmentCreatePostBottomSheetBinding? = null
    private val binding: FragmentCreatePostBottomSheetBinding get() = _binding!!
    private val chipSpoiler: Chip by lazy { binding.chipSpoiler }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBottomSheetBinding.inflate(inflater, container, false)
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
            chipSpoiler.id -> {
                handlerChipIcon(chipSpoiler.isChecked)
            }
        }
    }

    private fun handlerChipIcon(checked: Boolean) {
        chipSpoiler.chipIcon = if (checked) {
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_checked_outline)
        } else {
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_add)
        }
    }

    private fun initClicks() {
        binding.chipSpoiler.setOnClickListener(this)
    }
}