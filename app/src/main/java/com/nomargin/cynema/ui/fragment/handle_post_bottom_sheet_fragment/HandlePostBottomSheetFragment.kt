package com.nomargin.cynema.ui.fragment.handle_post_bottom_sheet_fragment

import android.content.Intent
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
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.databinding.FragmentHandlePostBottomSheetBinding
import com.nomargin.cynema.ui.activity.movie_discussion_post_activity.MovieDiscussionPostActivity
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.extension.OnSaveClickListener
import com.nomargin.cynema.util.extension.TextInputLayoutExtensions.setFieldError
import com.nomargin.cynema.util.model.StatusModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HandlePostBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: FragmentHandlePostBottomSheetBinding? = null
    private val binding: FragmentHandlePostBottomSheetBinding get() = _binding!!
    private val handlePostBottomSheetViewModel: HandlePostBottomSheetViewModel by viewModels()
    private var onSaveClickListener: OnSaveClickListener? = null
    private val chipSpoiler: Chip by lazy { binding.chipSpoiler }
    private val postTitleInputLayout: TextInputLayout by lazy { binding.postTitleInputLayout }
    private val postBodyInputLayout: TextInputLayout by lazy { binding.postBodyInputLayout }
    private val postTitle: TextInputEditText by lazy { binding.postTitleInput }
    private val postBody: TextInputEditText by lazy { binding.postBodyInput }
    private var handlerType: String? = null
    private var postId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHandlePostBottomSheetBinding.inflate(inflater, container, false)
        handlerType = arguments?.getString(
            Constants.BUNDLE_KEYS.MovieDiscussionHandlerType.name,
            Constants.BOTTOM_SHEET_TYPE.MovieDiscussionHandlerCreate.name
        )
        postId = arguments?.getString(
            Constants.BUNDLE_KEYS.MovieDiscussionPostId.name
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlerType()
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
                when (handlerType) {
                    Constants.BOTTOM_SHEET_TYPE.MovieDiscussionHandlerEdit.name -> {
                        handlePostBottomSheetViewModel.updatePost(
                            postId ?: "",
                            binding.postTitleInput.text.toString(),
                            binding.postBodyInput.text.toString(),
                            binding.chipSpoiler.isChecked
                        )
                        onSaveClickListener?.onSaveClicked()
                    }

                    else -> {
                        handlePostBottomSheetViewModel.publishPost(
                            binding.postTitleInput.text.toString(),
                            binding.postBodyInput.text.toString(),
                            binding.chipSpoiler.isChecked
                        )
                    }
                }
            }
        }
    }

    fun setOnSaveClickListener(listener: OnSaveClickListener) {
        onSaveClickListener = listener
    }

    private fun handlerType() {
        when (handlerType) {
            Constants.BOTTOM_SHEET_TYPE.MovieDiscussionHandlerCreate.name -> {
                binding.buttonPublish.text = getString(R.string.publish)
            }

            else -> {
                binding.buttonPublish.text = getString(R.string.save)
                handlePostBottomSheetViewModel.getDiscussionPostById(postId)
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
        handlePostBottomSheetViewModel.createPostStatus.observe(viewLifecycleOwner) { postStatus ->
            postStatus?.let {
                if (it.statusModel!!.isValid) {
                    this.dismiss()
                    goToPost(it.data ?: "")
                } else {
                    errorHandler(it.statusModel)
                }
            }
        }

        handlePostBottomSheetViewModel.updatePost.observe(viewLifecycleOwner) { postStatus ->
            postStatus?.let {
                if (it.statusModel!!.isValid) {
                    this.dismiss()
                    goToPost(it.data ?: "")
                } else {
                    errorHandler(it.statusModel)
                }
            }
        }

        handlePostBottomSheetViewModel.post.observe(viewLifecycleOwner) { postData ->
            handlerFields(postData)
        }
    }

    private fun handlerFields(postData: PostAppearanceModel?) {
        postData?.let {
            binding.postTitleInput.setText(it.title)
            binding.postBodyInput.setText(it.body)
            binding.chipSpoiler.isChecked = it.isSpoiler ?: false
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

    private fun goToPost(postId: String) {
        when (handlerType) {
            Constants.BOTTOM_SHEET_TYPE.MovieDiscussionHandlerEdit.name -> {
                this.dismiss()
            }

            else -> {
                val intent = Intent(requireContext(), MovieDiscussionPostActivity::class.java)
                val bundle = Bundle()
                bundle.putString(Constants.BUNDLE_KEYS.MovieDiscussionPostId.toString(), postId)
                intent.putExtras(bundle)
                startActivity(intent)

            }
        }
    }
}