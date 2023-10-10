package com.nomargin.cynema.ui.fragment.handle_post_bottom_sheet_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.data.local.entity.PostModel
import com.nomargin.cynema.data.repository.PostRepository
import com.nomargin.cynema.data.repository.SharedPreferencesRepository
import com.nomargin.cynema.data.usecase.PostUseCase
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HandlePostBottomSheetViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
    sharedPreferencesRepository: SharedPreferencesRepository,
    private val postRepository: PostRepository,
) : ViewModel() {


    private val movieId = sharedPreferencesRepository.getString(
        Constants.LOCAL_STORAGE.sharedPreferencesMovieIdKey,
        ""
    )
    private var _createPostStatus: MutableLiveData<Resource<String>> = MutableLiveData()
    val createPostStatus: LiveData<Resource<String>> = _createPostStatus
    private val _post: MutableLiveData<PostAppearanceModel?> = MutableLiveData()
    val post: LiveData<PostAppearanceModel?> = _post
    private val _updatePost: MutableLiveData<Resource<String>> = MutableLiveData()
    val updatePost: LiveData<Resource<String>> = _updatePost

    fun publishPost(
        title: String,
        body: String,
        checked: Boolean,
    ) = viewModelScope.launch {

        val postModel = PostModel(
            title = title,
            body = body,
            isSpoiler = checked,
            movieId = movieId ?: ""
        )
        _createPostStatus.value = postRepository.publishPost(postModel)

    }

    fun getDiscussionPostById(postId: String?) = viewModelScope.launch {
        _post.value = postUseCase.getPostById(postId ?: "")
    }

    fun updatePost(
        id: String,
        title: String,
        body: String,
        checked: Boolean,
    ) = viewModelScope.launch {
        val postModel = PostModel(
            id = id,
            title = title,
            body = body,
            isSpoiler = checked,
            movieId = movieId ?: ""
        )
        _updatePost.value = postRepository.updatePost(postModel)
    }

}