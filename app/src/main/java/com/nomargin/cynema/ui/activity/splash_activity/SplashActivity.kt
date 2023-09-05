package com.nomargin.cynema.ui.activity.splash_activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.nomargin.cynema.databinding.ActivitySplashBinding
import com.nomargin.cynema.ui.activity.auth_activity.AuthActivity
import com.nomargin.cynema.ui.activity.main_activity.MainActivity
import com.nomargin.cynema.util.FrequencyFunctions.startNewActivityFromCurrentActivity
import com.nomargin.cynema.util.Status
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels()
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        splashViewModel.verifyCurrentUser()
        observers()
    }

    private fun observers(){
        splashViewModel.currentUser.observe(this){status ->
            when(status){
                Status.SUCCESS -> {
                    startNewActivityFromCurrentActivity(this, MainActivity())
                }
                else -> {
                    startNewActivityFromCurrentActivity(this, AuthActivity())
                }
            }
        }
    }
}