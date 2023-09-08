package com.nomargin.cynema.ui.activity.create_profile_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.nomargin.cynema.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateProfileActivity : AppCompatActivity() {
    private val createProfileViewModel: CreateProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)
    }
}