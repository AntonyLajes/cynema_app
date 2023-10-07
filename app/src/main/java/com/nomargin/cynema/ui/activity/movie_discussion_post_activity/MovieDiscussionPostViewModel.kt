package com.nomargin.cynema.ui.activity.movie_discussion_post_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.data.repository.SharedPreferencesRepository
import com.nomargin.cynema.data.usecase.PostUseCase
import com.nomargin.cynema.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDiscussionPostViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : ViewModel() {

    private val _post: MutableLiveData<PostAppearanceModel?> = MutableLiveData()
    val post: LiveData<PostAppearanceModel?> = _post
    private val _getUpdatedPost: MutableLiveData<PostAppearanceModel?> = MutableLiveData()
    val getUpdatedPost: LiveData<PostAppearanceModel?> = _getUpdatedPost

    fun getDiscussionPostById(postId: String?) = viewModelScope.launch {
        _post.value = postUseCase.getPostById(postId ?: "")
    }

    fun updatePostVote(updateType: Constants.UPDATE_TYPE, postId: String) = viewModelScope.launch {
        _getUpdatedPost.value = postUseCase.updatePostVote(updateType, postId)
    }

    fun saveDataToSharedPreferences(key: String, postId: String){
        sharedPreferencesRepository.putString(key, postId)
    }

}