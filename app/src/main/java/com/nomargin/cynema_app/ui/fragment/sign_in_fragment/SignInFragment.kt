package com.nomargin.cynema_app.ui.fragment.sign_in_fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nomargin.cynema_app.databinding.FragmentSignInBinding
import com.nomargin.cynema_app.ui.activity.forgot_password_activity.ForgotPasswordActivity
import com.nomargin.cynema_app.ui.activity.main_activity.MainActivity

class SignInFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSignInBinding? = null
    private val binding: FragmentSignInBinding get() = _binding!!
    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initClicks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.buttonForgotPassword.id -> {
                verifyAndStartActivity(ForgotPasswordActivity())
            }
            binding.buttonSignIn.id -> {
                verifyAndStartActivity(MainActivity())
            }
        }
    }

    private fun verifyAndStartActivity(activity: Activity) {
        val intent = Intent(context, activity::class.java)
        startActivity(intent)
    }

    private fun initClicks() {
        binding.buttonForgotPassword.setOnClickListener(this)
        binding.buttonSignIn.setOnClickListener(this)
    }

}