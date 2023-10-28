package com.nomargin.cynema.ui.fragment.post_list_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.data.usecase.PostUseCase
import com.nomargin.cynema.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
) : ViewModel() {

    private var _userPost: MutableLiveData<List<PostAppearanceModel>> = MutableLiveData()
    val userPost: LiveData<List<PostAppearanceModel>> get() = _userPost
    private var _getUpdatedPost: MutableLiveData<PostAppearanceModel?> = MutableLiveData()
    val getUpdatedPost: LiveData<PostAppearanceModel?> = _getUpdatedPost

    fun getUserPosts(postIdList: List<String>) = viewModelScope.launch {
        _userPost.value = postUseCase.getPostByIdList(postIdList)
    }

    fun updatePostVote(updateType: Constants.UPDATE_TYPE, postId: String) = viewModelScope.launch {
        _getUpdatedPost.value = postUseCase.updatePostVote(updateType, postId)
    }
}