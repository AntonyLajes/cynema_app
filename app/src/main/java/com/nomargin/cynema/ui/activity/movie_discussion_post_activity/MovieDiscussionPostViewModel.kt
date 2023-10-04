package com.nomargin.cynema.ui.activity.movie_discussion_post_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.data.usecase.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDiscussionPostViewModel @Inject constructor(
    private val postUseCase: PostUseCase
) : ViewModel() {

    private val _post: MutableLiveData<PostAppearanceModel?> = MutableLiveData()
    val post: LiveData<PostAppearanceModel?> = _post

    fun getDiscussionPostById(postId: String?) = viewModelScope.launch {
        _post.value = postUseCase.getPostById(postId ?: "")
    }

}