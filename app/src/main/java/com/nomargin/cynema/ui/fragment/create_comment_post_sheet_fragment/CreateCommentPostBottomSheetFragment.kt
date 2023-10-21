package com.nomargin.cynema.ui.fragment.create_comment_post_sheet_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.CommentAppearanceModel
import com.nomargin.cynema.databinding.FragmentCreateCommentPostBottomSheetBinding
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Status
import com.nomargin.cynema.util.extension.OnCommentClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCommentPostBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: FragmentCreateCommentPostBottomSheetBinding? = null
    private val binding: FragmentCreateCommentPostBottomSheetBinding get() = _binding!!
    private val createCommentPostBottomSheetViewModel: CreateCommentPostBottomSheetViewModel by viewModels()
    private var onCommentClickListener: OnCommentClickListener? = null
    private var handlerType: String? = null
    private var commentId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateCommentPostBottomSheetBinding.inflate(inflater, container, false)
        handlerType = arguments?.getString(
            Constants.BUNDLE_KEYS.MovieDiscussionHandlerType.name
        )
        commentId = arguments?.getString(
            Constants.BUNDLE_KEYS.MovieDiscussionPostCommentId.name
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldsHandler()
        observers()
        initClicks()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.buttonComment.id -> {

                when (handlerType) {
                    Constants.BOTTOM_SHEET_TYPE.MovieDiscussionHandlerEdit.name -> {
                        createCommentPostBottomSheetViewModel.updateComment(
                            commentId ?: "",
                            binding.commentBodyInput.text.toString(),
                            binding.chipSpoiler.isChecked
                        )
                    }

                    else -> {
                        createCommentPostBottomSheetViewModel.publishComment(
                            binding.commentBodyInput.text.toString(),
                            binding.chipSpoiler.isChecked
                        )
                    }
                }

            }
        }
    }

    fun setOnCommentClickListener(listener: OnCommentClickListener) {
        onCommentClickListener = listener
    }

    private fun initClicks() {
        binding.buttonComment.setOnClickListener(this)
    }

    private fun observers() {
        createCommentPostBottomSheetViewModel.createCommentStatus.observe(this) { createCommentStatus ->
            if (createCommentStatus.status == Status.SUCCESS) {
                onCommentClickListener?.onAddCommentClicked()
                closeBottomSheet()
            } else {
                Log.d("createCommentStatus", "observers: ${createCommentStatus.statusModel}")
            }
        }
        createCommentPostBottomSheetViewModel.updateComment.observe(this) { updateCommentStatus ->
            if (updateCommentStatus.status == Status.SUCCESS) {
                onCommentClickListener?.onAddCommentClicked()
                closeBottomSheet()
            } else {
                Log.d("updateCommentStatus", "observers: ${updateCommentStatus.statusModel}")
            }
        }
        createCommentPostBottomSheetViewModel.comment.observe(this) { comment ->
            commentDataHandler(comment)
        }
    }

    private fun commentDataHandler(comment: CommentAppearanceModel?) {
        comment?.let {
            binding.commentBodyInput.setText(it.body)
            binding.chipSpoiler.isChecked = it.isSpoiler ?: false
        }
    }

    private fun fieldsHandler() {
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
        when (handlerType) {
            Constants.BOTTOM_SHEET_TYPE.MovieDiscussionHandlerEdit.name -> {
                binding.descStart.text = buildString {
                    append(requireActivity().getString(R.string.editing_your_comment_on))
                    append(" ")
                }
                binding.buttonComment.text = getString(R.string.save)
                createCommentPostBottomSheetViewModel.getCommentById(commentId ?: "")
            }

            else -> {
                binding.buttonComment.text = getString(R.string.add_comment)
                binding.descStart.text = buildString {
                    append(requireActivity().getString(R.string.commenting_on))
                    append(" ")
                }
            }
        }
    }

    private fun closeBottomSheet() {
        this.dismiss()
    }
}