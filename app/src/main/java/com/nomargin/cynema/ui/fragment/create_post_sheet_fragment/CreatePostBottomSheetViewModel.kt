package com.nomargin.cynema.ui.fragment.create_post_sheet_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.local.PostModel
import com.nomargin.cynema.data.repository.PostRepository
import com.nomargin.cynema.data.repository.SharedPreferencesRepository
import com.nomargin.cynema.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostBottomSheetViewModel @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    fun publishPost(title: String, body: String) = viewModelScope.launch {
        val movieId = sharedPreferencesRepository.getString(
            Constants.LOCAL_STORAGE.sharedPreferencesMovieIdKey,
            ""
        )
        val postModel = PostModel(
            title,
            body,
            movieId ?: ""
        )
        postRepository.publishPost(postModel)
    }

}