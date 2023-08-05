package com.nomargin.cynema_app.ui.activity.forgot_password_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nomargin.cynema_app.R
import com.nomargin.cynema_app.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private val binding: ActivityForgotPasswordBinding by lazy {ActivityForgotPasswordBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}