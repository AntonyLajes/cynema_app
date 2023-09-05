package com.nomargin.cynema.ui.activity.forgot_password_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.nomargin.cynema.databinding.ActivityForgotPasswordBinding
import com.nomargin.cynema.ui.activity.auth_activity.AuthActivity
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.FrequencyFunctions
import com.nomargin.cynema.util.FrequencyFunctions.makeToast
import com.nomargin.cynema.util.extension.TextInputLayoutExtensions.setFieldError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()
    private val binding: ActivityForgotPasswordBinding by lazy {
        ActivityForgotPasswordBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        inputWatcher()
        observers()
        initClicks()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.buttonSendEmail.id -> {
                forgotPasswordViewModel.sendPasswordResetEmail(binding.emailInput.text.toString())
            }

            binding.buttonCancel.id -> {
                FrequencyFunctions.startNewActivityFromCurrentActivity(this, AuthActivity())
            }
        }
    }

    private fun initClicks(){
        binding.buttonSendEmail.setOnClickListener(this)
        binding.buttonCancel.setOnClickListener(this)
    }

    private fun observers() {
        forgotPasswordViewModel.sendPasswordResetEmailStatus.observe(this) { attributeStatus ->
            attributeStatus?.let {
                if (it.isValid) {
                    makeToast(this, it.message)
                    FrequencyFunctions.startNewActivityFromCurrentActivity(this, AuthActivity())
                } else {
                    when (it.errorType) {
                        Constants.ERROR_TYPES.emailFieldIsEmpty -> {
                            binding.emailInputLayout.setFieldError(it.message)
                        }

                        else -> {
                            makeToast(this, it.message)
                        }
                    }
                }
            }
        }
    }

    private fun inputWatcher() {
        binding.emailInput.doAfterTextChanged { text ->
            if (text.toString().isNotEmpty()) {
                binding.emailInputLayout.setFieldError(null)
            }
        }
    }
}