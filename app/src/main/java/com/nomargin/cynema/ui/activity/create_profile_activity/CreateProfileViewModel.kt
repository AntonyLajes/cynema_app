package com.nomargin.cynema.ui.activity.create_profile_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.remote.firebase.entity.UserProfileDataModel
import com.nomargin.cynema.data.repository.ProfileRepository
import com.nomargin.cynema.util.Resource
import com.nomargin.cynema.util.model.StatusModel
import com.nomargin.cynema.util.model.UserProfileModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private var _createProfileStatus: MutableLiveData<StatusModel> = MutableLiveData()
    val createProfileStatus: LiveData<StatusModel> = _createProfileStatus

    private var _userUsernameStatus: MutableLiveData<StatusModel?> = MutableLiveData()
    val userUsernameStatus: LiveData<StatusModel?> = _userUsernameStatus

    private var _userData: MutableLiveData<Resource<UserProfileDataModel>> = MutableLiveData()
    val userData: LiveData<Resource<UserProfileDataModel>> get() = _userData

    fun saveProfile(userProfileModel: UserProfileModel) = viewModelScope.launch {
        _createProfileStatus.value = profileRepository.createProfile(userProfileModel).statusModel!!
    }

    fun checkUserUsername(userUsername: String) = viewModelScope.launch {
        _userUsernameStatus.value = profileRepository.checkUserUsername(userUsername)
    }

    fun getUserData(userId: String) = viewModelScope.launch {
        _userData.value = profileRepository.getUserData(userId)
    }

}