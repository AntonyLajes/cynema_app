package com.nomargin.cynema.ui.fragment.sign_in_fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.entity.SignInModel
import com.nomargin.cynema.databinding.FragmentSignInBinding
import com.nomargin.cynema.ui.activity.forgot_password_activity.ForgotPasswordActivity
import com.nomargin.cynema.ui.activity.main_activity.MainActivity
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.FrequencyFunctions
import com.nomargin.cynema.util.extension.TextInputLayoutExtensions.setFieldError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment(), View.OnClickListener {

    private val signInViewModel: SignInViewModel by viewModels()
    private var _binding: FragmentSignInBinding? = null
    private val binding: FragmentSignInBinding get() = _binding!!
    private lateinit var oneTapClient: SignInClient
    private lateinit var authenticationRequest: BeginSignInRequest
    private val oneTapSignInResultLauncher: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                signInViewModel.verifyIdToken(
                    result,
                    oneTapClient
                )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputWatcher()
        setMovementMethod()
        observers()
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
                signInViewModel.signIn(
                    SignInModel(
                        binding.emailInput.text.toString(),
                        binding.passwordInput.text.toString()
                    )
                )
            }

            binding.signInWithGoogle.id -> {
                initOneTapAuthentication()
            }
        }
    }

    private fun observers() {
        signInViewModel.oneTapStatus.observe(viewLifecycleOwner) { oneTapStatus ->
            if (oneTapStatus.isValid) {
                FrequencyFunctions.makeToast(requireContext(), oneTapStatus.message)
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                FrequencyFunctions.makeToast(requireContext(), oneTapStatus.message)
            }
        }
        signInViewModel.attributesStatus.observe(viewLifecycleOwner) { attributeStatus ->
            attributeStatus?.let {
                if (it.isValid) {
                    makeToast(it.message)
                    verifyAndStartActivity(MainActivity())
                } else {
                    when (it.errorType) {
                        Constants.ERROR_TYPES.emailFieldIsEmpty -> {
                            binding.emailInputLayout.setFieldError(it.message)
                        }

                        Constants.ERROR_TYPES.passwordFieldIsEmpty -> {
                            binding.passwordInputLayout.setFieldError(it.message)
                        }

                        else -> {
                            makeToast(it.message)
                        }
                    }
                }
            }
        }
    }

    private fun makeToast(@StringRes messageRes: Int) {
        Toast.makeText(requireActivity(), getText(messageRes), Toast.LENGTH_SHORT).show()
    }

    private fun initOneTapAuthentication() {
        oneTapClient = Identity.getSignInClient(requireActivity())
        lifecycleScope.launch {
            authenticationRequest = signInViewModel.beginAuthenticationRequest()
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

    private fun verifyAndStartActivity(activity: Activity) {
        val intent = Intent(context, activity::class.java)
        startActivity(intent)
    }

    private fun initClicks() {
        binding.buttonForgotPassword.setOnClickListener(this)
        binding.buttonSignIn.setOnClickListener(this)
        binding.signInWithGoogle.setOnClickListener(this)
    }

    private fun setMovementMethod() {
        binding.labelTermsConditionsPrivacyPolicyAdvise.movementMethod =
            LinkMovementMethod.getInstance()
        binding.labelTermsConditionsPrivacyPolicyAdvise.setLinkTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.color_primary
            )
        )
    }

    private fun inputWatcher(){
        binding.emailInput.doAfterTextChanged { text ->
            if (text.toString().isNotEmpty()) {
                binding.emailInputLayout.setFieldError(null)
            }
        }

        binding.passwordInput.doAfterTextChanged { text ->
            if (text.toString().isNotEmpty()) {
                binding.passwordInputLayout.setFieldError(null)
            }
        }
    }

}