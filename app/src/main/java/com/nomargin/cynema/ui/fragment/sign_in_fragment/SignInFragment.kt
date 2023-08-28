package com.nomargin.cynema.ui.fragment.sign_in_fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nomargin.cynema.R
import com.nomargin.cynema.databinding.FragmentSignInBinding
import com.nomargin.cynema.ui.activity.forgot_password_activity.ForgotPasswordActivity
import com.nomargin.cynema.ui.activity.main_activity.MainActivity

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMovementMethod()
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

    private fun setMovementMethod(){
        binding.labelTermsConditionsPrivacyPolicyAdvise.movementMethod = LinkMovementMethod.getInstance()
        binding.labelTermsConditionsPrivacyPolicyAdvise.setLinkTextColor(ContextCompat.getColor(requireContext(), R.color.color_primary))
    }

}