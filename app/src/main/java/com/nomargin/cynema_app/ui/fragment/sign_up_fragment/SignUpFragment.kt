package com.nomargin.cynema_app.ui.fragment.sign_up_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nomargin.cynema_app.R
import com.nomargin.cynema_app.data.remote.entity.SignUpModel
import com.nomargin.cynema_app.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val signUpViewModel: SignUpViewModel by viewModels()
    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
    }

    private fun initClicks() {
        binding.buttonSignUp.setOnClickListener { signUp() }
    }

    private fun signUp() {
        signUpViewModel.signUp(
            SignUpModel(
                email = binding.emailInput.text.toString(),
                password = binding.passwordInput.text.toString(),
                confirmPassword = binding.confirmPasswordInput.text.toString()
            )
        )
    }

}