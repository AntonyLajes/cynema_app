package com.nomargin.cynema.ui.activity.create_profile_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.repository.ProfileRepository
import com.nomargin.cynema.util.model.StatusModel
import com.nomargin.cynema.util.model.UserProfileModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
): ViewModel() {
    private var _attributeStatus:MutableLiveData<StatusModel> = MutableLiveData()
    val attributeStatus: LiveData<StatusModel> = _attributeStatus
    fun saveProfile(userProfileModel: UserProfileModel) = viewModelScope.launch {
        _attributeStatus.value = profileRepository.createProfile(userProfileModel).statusModel!!
    }


}