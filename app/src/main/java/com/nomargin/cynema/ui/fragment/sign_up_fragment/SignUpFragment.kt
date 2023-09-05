package com.nomargin.cynema.ui.fragment.sign_up_fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.entity.SignUpModel
import com.nomargin.cynema.databinding.FragmentSignUpBinding
import com.nomargin.cynema.ui.activity.main_activity.MainActivity
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.FrequencyFunctions
import com.nomargin.cynema.util.model.StatusModel
import com.nomargin.cynema.util.extension.TextInputLayoutExtensions.setFieldError
import com.nomargin.cynema.util.extension.TextViewExtensions.changeDrawableColor
import com.nomargin.cynema.util.extension.TextViewExtensions.changeStartDrawable
import com.nomargin.cynema.util.extension.TextViewExtensions.changeTextColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment(), View.OnClickListener {

    private val signUpViewModel: SignUpViewModel by viewModels()
    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding get() = _binding!!
    private val email: TextInputEditText by lazy { binding.emailInput }
    private val password: TextInputEditText by lazy { binding.passwordInput }
    private val confirmPassword: TextInputEditText by lazy { binding.confirmPasswordInput }
    private val emailLayout: TextInputLayout by lazy { binding.emailInputLayout }
    private val passwordLayout: TextInputLayout by lazy { binding.passwordInputLayout }
    private val confirmPasswordLayout: TextInputLayout by lazy { binding.confirmPasswordInputLayout }
    private lateinit var oneTapClient: SignInClient
    private lateinit var authenticationRequest: BeginSignInRequest
    private val oneTapSignInResultLauncher: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                signUpViewModel.verifyIdToken(
                    result,
                    oneTapClient
                )
            }
        }

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
        setMovementMethod()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.buttonSignUp.id -> {
                signUp()
            }

            binding.signUpWithGoogle.id -> {
                initOneTapAuthentication()
            }
        }
    }

    private fun initClicks() {
        binding.buttonSignUp.setOnClickListener(this)
        binding.signUpWithGoogle.setOnClickListener(this)
    }

    private fun signUp() {
        signUpViewModel.signUp(
            SignUpModel(
                email = email.text.toString(),
                password = password.text.toString(),
                confirmPassword = confirmPassword.text.toString(),
                acceptedTermsAndPrivacyPolicy = binding.checkboxTermsConditionsPrivacyPolicy.isChecked
            )
        )
    }

    private fun initOneTapAuthentication() {
        oneTapClient = Identity.getSignInClient(requireActivity())
        lifecycleScope.launch {
            authenticationRequest = signUpViewModel.beginAuthenticationRequest()
            oneTapClient.beginSignIn(authenticationRequest)
                .addOnSuccessListener(requireActivity()) { result ->
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapSignInResultLauncher.launch(intentSenderRequest)
                }
                .addOnFailureListener(requireActivity()) { e ->
                    // No Google Accounts found. Just continue presenting the signed-out UI.
                    e.localizedMessage?.let { Log.d("authenticationRequest", it) }
                }
        }
    }

    private fun observers() {
        signUpViewModel.attributesStatus.observe(viewLifecycleOwner) { attributesStatus ->
            attributesStatus?.let {
                if (attributesStatus.isValid) {
                    FrequencyFunctions.makeToast(requireContext(), attributesStatus.message)
                }
                fieldsHandler(attributesStatus)
            }
        }
        signUpViewModel.oneTapStatus.observe(viewLifecycleOwner) { oneTapStatus ->
            if (oneTapStatus.isValid) {
                FrequencyFunctions.makeToast(requireContext(), oneTapStatus.message)
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                FrequencyFunctions.makeToast(requireContext(), oneTapStatus.message)
            }
        }
    }

    private fun fieldsHandler(value: StatusModel) {
        binding.checkboxTermsConditionsPrivacyPolicy.error = null
        if (value.isValid) {
            email.text?.clear()
            password.text?.clear()
            confirmPassword.text?.clear()
            binding.checkboxTermsConditionsPrivacyPolicy.isChecked = false
        } else {
            when (value.errorType) {
                Constants.ERROR_TYPES.emailFieldIsEmpty -> {
                    emailLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.passwordFieldIsEmpty -> {
                    passwordLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.passwordShouldHaveMoreThanEightCharacters -> {
                    passwordLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.confirmPasswordFieldIsEmpty -> {
                    confirmPasswordLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.passwordsDoNotMatch -> {
                    passwordLayout.setFieldError(value.message)
                    confirmPasswordLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.theUserDidNotAcceptedTermsOfUseAndPrivacyPolicy -> {
                    binding.checkboxTermsConditionsPrivacyPolicy.error =
                        requireContext().getString(value.message)
                }
                Constants.ERROR_TYPES.firebaseAuthError -> {
                    FrequencyFunctions.makeToast(requireContext(), value.message)
                }
            }
        }
    }

    private fun inputWatcher() {
        email.doAfterTextChanged { text ->
            if (text.toString().isNotEmpty()) {
                emailLayout.setFieldError(null)
            }
        }
        password.doAfterTextChanged { text ->
            binding.passwordRequirementsField.visibility = if (text.toString().isNotEmpty()) {
                passwordLayout.setFieldError(null)
                View.VISIBLE
            } else {
                View.GONE
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
        confirmPassword.doAfterTextChanged { text ->
            if (text.toString().isNotEmpty()) {
                confirmPasswordLayout.setFieldError(null)
            }
        }
    }

    private fun setMovementMethod() {
        binding.checkboxTermsConditionsPrivacyPolicy.movementMethod =
            LinkMovementMethod.getInstance()
        binding.checkboxTermsConditionsPrivacyPolicy.setLinkTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.color_primary
            )
        )
    }

}