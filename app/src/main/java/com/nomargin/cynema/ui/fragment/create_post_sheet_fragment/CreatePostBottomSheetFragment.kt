package com.nomargin.cynema.ui.fragment.create_post_sheet_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nomargin.cynema.R
import com.nomargin.cynema.databinding.FragmentCreatePostBottomSheetBinding
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.extension.TextInputLayoutExtensions.setFieldError
import com.nomargin.cynema.util.model.StatusModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePostBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: FragmentCreatePostBottomSheetBinding? = null
    private val binding: FragmentCreatePostBottomSheetBinding get() = _binding!!
    private val createPostBottomSheetViewModel: CreatePostBottomSheetViewModel by viewModels()
    private val chipSpoiler: Chip by lazy { binding.chipSpoiler }
    private val postTitleInputLayout: TextInputLayout by lazy { binding.postTitleInputLayout }
    private val postBodyInputLayout: TextInputLayout by lazy { binding.postBodyInputLayout }
    private val postTitle: TextInputEditText by lazy { binding.postTitleInput }
    private val postBody: TextInputEditText by lazy { binding.postBodyInput }

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
        focusWatcher()
        inputWatcher()
        observers()
        initClicks()
    }

    override fun onClick(view: View) {
        when (view.id) {
            chipSpoiler.id -> {
                handlerChipIcon(chipSpoiler.isChecked)
            }

            binding.buttonPublish.id -> {
                createPostBottomSheetViewModel.publishPost(
                    binding.postTitleInput.text.toString(),
                    binding.postBodyInput.text.toString(),
                    binding.chipSpoiler.isChecked
                )
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
        binding.buttonPublish.setOnClickListener(this)
    }

    private fun focusWatcher() {
        postTitleInputLayout.isCounterEnabled = postTitleInputLayout.hasFocus()
        postBodyInputLayout.isCounterEnabled = postBodyInputLayout.hasFocus()
    }

    private fun observers() {
        createPostBottomSheetViewModel.createPostStatus.observe(viewLifecycleOwner) { postStatus ->
            postStatus?.let {
                if (it.isValid) {
                    
                } else {
                    errorHandler(it)
                }
            }
        }
    }

    private fun errorHandler(value: StatusModel) {
        if (!value.isValid) {
            when (value.errorType) {
                Constants.ERROR_TYPES.postTitleIsEmpty -> {
                    postTitleInputLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.postTitleIsBiggerThanAllowed -> {
                    postTitleInputLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.postBodyIsEmpty -> {
                    postBodyInputLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.postBodyIsBiggerThanAllowed -> {
                    postBodyInputLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.postTitleIsLowerThanAllowed -> {
                    postTitleInputLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.postBodyIsLowerThanAllowed -> {
                    postBodyInputLayout.setFieldError(value.message)
                }
            }
        }
    }

    private fun inputWatcher() {
        postTitle.doAfterTextChanged { text ->
            postTitleInputLayout.isCounterEnabled = if (text.toString().isNotEmpty()) {
                postTitleInputLayout.setFieldError(null)
                true
            } else {
                false
            }
        }
        postBody.doAfterTextChanged { text ->
            postBodyInputLayout.isCounterEnabled = if (text.toString().isNotEmpty()) {
                postBodyInputLayout.setFieldError(null)
                true
            } else {
                false
            }
        }
    }
}