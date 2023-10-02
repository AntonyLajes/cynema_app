package com.nomargin.cynema.ui.fragment.create_post_sheet_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.local.PostModel
import com.nomargin.cynema.data.repository.PostRepository
import com.nomargin.cynema.data.repository.SharedPreferencesRepository
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.model.StatusModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostBottomSheetViewModel @Inject constructor(
    sharedPreferencesRepository: SharedPreferencesRepository,
    private val postRepository: PostRepository
) : ViewModel() {


    private val movieId = sharedPreferencesRepository.getString(
        Constants.LOCAL_STORAGE.sharedPreferencesMovieIdKey,
        ""
    )
    private var _createPostStatus: MutableLiveData<StatusModel> = MutableLiveData()
    val createPostStatus: LiveData<StatusModel> = _createPostStatus
    fun publishPost(title: String, body: String, checked: Boolean) = viewModelScope.launch {

        val postModel = PostModel(
            title,
            body,
            checked,
            movieId ?: ""
        )
        _createPostStatus.value = postRepository.publishPost(postModel).statusModel!!

    }

}