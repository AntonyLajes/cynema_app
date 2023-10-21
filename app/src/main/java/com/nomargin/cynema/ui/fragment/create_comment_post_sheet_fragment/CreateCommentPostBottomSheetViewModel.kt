package com.nomargin.cynema.ui.fragment.create_comment_post_sheet_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.local.entity.CommentAppearanceModel
import com.nomargin.cynema.data.local.entity.CommentModel
import com.nomargin.cynema.data.repository.CommentRepository
import com.nomargin.cynema.data.repository.SharedPreferencesRepository
import com.nomargin.cynema.data.usecase.CommentUseCase
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCommentPostBottomSheetViewModel @Inject constructor(
    sharedPreferencesRepository: SharedPreferencesRepository,
    private val commentRepository: CommentRepository,
    private val commentUseCase: CommentUseCase,
) : ViewModel() {

    private val postId = sharedPreferencesRepository.getString(
        Constants.LOCAL_STORAGE.sharedPreferencesPostIdKey,
        ""
    )

    private var _createCommentStatus: MutableLiveData<Resource<String>> = MutableLiveData()
    val createCommentStatus: LiveData<Resource<String>> = _createCommentStatus
    private val _updateComment: MutableLiveData<Resource<String>> = MutableLiveData()
    val updateComment: LiveData<Resource<String>> = _updateComment
    private val _comment: MutableLiveData<CommentAppearanceModel?> = MutableLiveData()
    val comment: LiveData<CommentAppearanceModel?> = _comment

    fun publishComment(body: String, isSpoiler: Boolean) = viewModelScope.launch {

        val commentModel = CommentModel(
            body = body,
            isSpoiler = isSpoiler,
            postId = postId ?: ""
        )
        _createCommentStatus.value = commentRepository.publishComment(commentModel)

    }

    fun updateComment(id: String, body: String, isSpoiler: Boolean) = viewModelScope.launch {
        val commentModel = CommentModel(
            id = id,
            body = body,
            isSpoiler = isSpoiler,
            postId = postId ?: ""
        )
        _updateComment.value = commentRepository.updateComment(commentModel)
    }

    fun getCommentById(id: String) = viewModelScope.launch {
        _comment.value = commentUseCase.getCommentById(id)
    }
}