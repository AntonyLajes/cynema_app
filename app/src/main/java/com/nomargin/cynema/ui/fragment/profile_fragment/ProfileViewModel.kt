package com.nomargin.cynema.ui.fragment.profile_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.remote.firebase.entity.UserProfileDataModel
import com.nomargin.cynema.data.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private var profileUseCase: ProfileUseCase,
) : ViewModel() {

    private var _userData: MutableLiveData<UserProfileDataModel?> = MutableLiveData()
    val userData: LiveData<UserProfileDataModel?> = _userData

    fun getUserData() = viewModelScope.launch {
        _userData.value = profileUseCase.getUserData()
    }

}