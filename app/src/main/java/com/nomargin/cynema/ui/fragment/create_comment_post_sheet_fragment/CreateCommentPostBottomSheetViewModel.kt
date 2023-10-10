package com.nomargin.cynema.ui.fragment.create_comment_post_sheet_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.local.entity.CommentModel
import com.nomargin.cynema.data.repository.CommentRepository
import com.nomargin.cynema.data.repository.SharedPreferencesRepository
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCommentPostBottomSheetViewModel @Inject constructor(
    sharedPreferencesRepository: SharedPreferencesRepository,
    private val commentRepository: CommentRepository,
) : ViewModel() {

    private val postId = sharedPreferencesRepository.getString(
        Constants.LOCAL_STORAGE.sharedPreferencesPostIdKey,
        ""
    )

    private var _createCommentStatus: MutableLiveData<Resource<String>> = MutableLiveData()
    val createCommentStatus: LiveData<Resource<String>> = _createCommentStatus

    fun publishComment(body: String, isSpoiler: Boolean) = viewModelScope.launch {

        val commentModel = CommentModel(
            body,
            isSpoiler,
            postId ?: ""
        )
        _createCommentStatus.value = commentRepository.publishComment(commentModel)

    }
}