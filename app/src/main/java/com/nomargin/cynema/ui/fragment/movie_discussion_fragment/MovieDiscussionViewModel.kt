package com.nomargin.cynema.ui.fragment.movie_discussion_fragment

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
class MovieDiscussionViewModel @Inject constructor(
    sharedPreferencesRepository: SharedPreferencesRepository,
    private val postUseCase: PostUseCase
) : ViewModel() {

    private val movieId = sharedPreferencesRepository.getString(
        Constants.LOCAL_STORAGE.sharedPreferencesMovieIdKey,
        ""
    )
    private var _getPosts: MutableLiveData<List<PostAppearanceModel>> = MutableLiveData()
    val getPosts: LiveData<List<PostAppearanceModel>> = _getPosts

    fun getPosts() = viewModelScope.launch {
        _getPosts.value = postUseCase.getPosts(movieId ?: "")
    }

}