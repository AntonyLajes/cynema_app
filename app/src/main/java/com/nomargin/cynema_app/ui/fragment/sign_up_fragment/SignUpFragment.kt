package com.nomargin.cynema_app.ui.fragment.sign_up_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.nomargin.cynema_app.R
import com.nomargin.cynema_app.data.remote.entity.SignUpModel
import com.nomargin.cynema_app.databinding.FragmentSignUpBinding
import com.nomargin.cynema_app.util.Constants
import com.nomargin.cynema_app.util.StatusModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val signUpViewModel: SignUpViewModel by viewModels()
    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding get() = _binding!!
    private val email: TextInputEditText by lazy { binding.emailInput }
    private val password: TextInputEditText by lazy { binding.passwordInput }
    private val confirmPassword: TextInputEditText by lazy { binding.confirmPasswordInput }

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
        observers()
        inputWatcher()
    }

    private fun initClicks() {
        binding.buttonSignUp.setOnClickListener { signUp() }
    }

    private fun signUp() {
        signUpViewModel.signUp(
            SignUpModel(
                email = email.text.toString(),
                password = password.text.toString(),
                confirmPassword = confirmPassword.text.toString()
            )
        )
    }

    private fun observers() {
        signUpViewModel.attributesStatus.observe(viewLifecycleOwner) { status ->
            if (status.isValid) {
                makeToast(status.message)
            } else {
                makeToast(status.message)
            }
            fieldsHandler(status)
        }
    }

    private fun makeToast(message: Int) {
        Toast.makeText(requireContext(), getString(message), Toast.LENGTH_SHORT).show()
    }

    private fun fieldsHandler(value: StatusModel) {
        if (value.isValid) {
            email.text?.clear()
            password.text?.clear()
            confirmPassword.text?.clear()
        } else {

        }
    }

    private fun inputWatcher() {
        password.doAfterTextChanged { text ->
            binding.passwordRequirementsField.visibility = if (text.toString().isNotEmpty()) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }

            if (Regex(Constants.REGEX.passwordRegexPattern).containsMatchIn(text.toString())) {
                binding.passwordRequirementsField.changeTextColor(R.color.color_primary)
                binding.passwordRequirementsField.changeStartDrawable(R.drawable.ic_checked)
                binding.passwordRequirementsField.changeDrawableColor(R.color.color_primary)
            } else {
                binding.passwordRequirementsField.changeTextColor(R.color.custom_normal_grey)
                binding.passwordRequirementsField.changeStartDrawable(R.drawable.ic_unchecked)
                binding.passwordRequirementsField.changeDrawableColor(R.color.custom_normal_grey)
            }
        }
    }

    private fun TextView.changeTextColor(@ColorRes color: Int){
        setTextColor(
            ContextCompat.getColor(
                requireContext(),
                color
            )
        )
    }

    private fun TextView.changeDrawableColor(@ColorRes color: Int) {
        compoundDrawables.filterNotNull()
            .forEach { drawable ->
                drawable.mutate()
                drawable.setTint(
                    ContextCompat.getColor(
                        requireContext(),
                        color
                    )
                )
            }
    }

    private fun TextView.changeStartDrawable(@DrawableRes drawable: Int){
        val imgDrawable = AppCompatResources.getDrawable(requireContext(), drawable)
        setCompoundDrawablesWithIntrinsicBounds(imgDrawable, null, null, null)
    }

}