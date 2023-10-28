package com.nomargin.cynema.ui.activity.movie_discussion_post_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.local.entity.CommentAppearanceModel
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.repository.CommentRepository
import com.nomargin.cynema.data.repository.SharedPreferencesRepository
import com.nomargin.cynema.data.usecase.CommentUseCase
import com.nomargin.cynema.data.usecase.PostUseCase
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.model.StatusModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDiscussionPostViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val commentUseCase: CommentUseCase,
    private val firebaseAuth: FirebaseAuthUseCase,
    private val commentRepository: CommentRepository,
) : ViewModel() {

    private val _post: MutableLiveData<PostAppearanceModel?> = MutableLiveData()
    val post: LiveData<PostAppearanceModel?> = _post
    private val _getUpdatedPost: MutableLiveData<PostAppearanceModel?> = MutableLiveData()
    val getUpdatedPost: LiveData<PostAppearanceModel?> = _getUpdatedPost
    private var _getComments: MutableLiveData<List<CommentAppearanceModel>?> = MutableLiveData()
    val getComments: LiveData<List<CommentAppearanceModel>?> = _getComments
    private val _getUpdatedComment: MutableLiveData<CommentAppearanceModel?> = MutableLiveData()
    val getUpdatedComment: LiveData<CommentAppearanceModel?> = _getUpdatedComment
    private val _deletePost: MutableLiveData<StatusModel> = MutableLiveData()
    val deletePost: LiveData<StatusModel?> = _deletePost
    private val _currentUserIsPostOwner: MutableLiveData<Boolean> = MutableLiveData()
    val currentUserIsPostOwner: LiveData<Boolean> = _currentUserIsPostOwner
    private val _deleteComment: MutableLiveData<StatusModel> = MutableLiveData()
    val deleteComment: LiveData<StatusModel?> = _deleteComment

    fun getDiscussionPostById(postId: String?) = viewModelScope.launch {
        _post.value = postUseCase.getPostById(postId ?: "")
    }

    fun updatePostVote(updateType: Constants.UPDATE_TYPE, postId: String) = viewModelScope.launch {
        _getUpdatedPost.value = postUseCase.updatePostVote(updateType, postId)
    }

    fun updateCommentVote(
        updateType: Constants.UPDATE_TYPE,
        commentId: String,
    ) = viewModelScope.launch {
        _getUpdatedComment.value = commentUseCase.updateCommentVote(updateType, commentId)
    }

    fun saveDataToSharedPreferences(key: String, postId: String) {
        sharedPreferencesRepository.putString(key, postId)
    }

    fun getAllComments(postId: String?) = viewModelScope.launch {
        _getComments.value = commentUseCase.getAllComments(postId ?: "")
    }

    fun deletePost(postId: String?) = viewModelScope.launch {
        _deletePost.value = postUseCase.deletePost(postId ?: "")
    }

    fun checkIfCurrentUserIsPostOwner(postOwnerId: String) {
        _currentUserIsPostOwner.value =
            firebaseAuth.getFirebaseAuth().currentUser?.uid.toString() == postOwnerId
    }

    fun deleteComment(id: String) = viewModelScope.launch {
        _deleteComment.value = commentRepository.deleteComment(id)
    }


}