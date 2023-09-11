package com.nomargin.cynema.ui.activity.auth_activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nomargin.cynema.R
import com.nomargin.cynema.databinding.ActivityAuthBinding
import com.nomargin.cynema.ui.activity.create_profile_activity.CreateProfileActivity
import com.nomargin.cynema.ui.activity.main_activity.MainActivity
import com.nomargin.cynema.ui.adapter.view_pager.ViewPagerAdapter
import com.nomargin.cynema.ui.fragment.sign_in_fragment.SignInFragment
import com.nomargin.cynema.ui.fragment.sign_up_fragment.SignUpFragment
import com.nomargin.cynema.util.FrequencyFunctions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val binding: ActivityAuthBinding by lazy { ActivityAuthBinding.inflate(layoutInflater) }
    private val viewPagerAdapter: ViewPagerAdapter by lazy { ViewPagerAdapter(this) }
    private val tabLayout: TabLayout by lazy { binding.tabLayout }
    private val viewPager: ViewPager2 by lazy { binding.viewPager }
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var oneTapClient: SignInClient
    private lateinit var authenticationRequest: BeginSignInRequest
    private val oneTapSignInResultLauncher: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                authViewModel.verifyIdToken(
                    result,
                    oneTapClient
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observers()
        viewPagerInflater()
        initOneTapAuthentication()
    }

    private fun viewPagerInflater() {
        viewPager.adapter = viewPagerAdapter
        viewPagerAdapter.setFragment(SignInFragment(), getString(R.string.sign_in_fragment_name))
        viewPagerAdapter.setFragment(SignUpFragment(), getString(R.string.sign_up_fragment_name))
        viewPager.offscreenPageLimit = viewPagerAdapter.itemCount
        val mediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewPagerAdapter.getTitle(position)
        }
        mediator.attach()
    }

    private fun initOneTapAuthentication() {
        oneTapClient = Identity.getSignInClient(this)
        lifecycleScope.launch {
            authenticationRequest = authViewModel.beginAuthenticationRequest()
            oneTapClient.beginSignIn(authenticationRequest)
                .addOnSuccessListener(this@AuthActivity) { result ->
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapSignInResultLauncher.launch(intentSenderRequest)
                }
                .addOnFailureListener(this@AuthActivity) { e ->
                    // No Google Accounts found. Just continue presenting the signed-out UI.
                    e.localizedMessage?.let { Log.d("authenticationRequest", it) }
                }
        }
    }

    private fun observers() {
        authViewModel.oneTapStatus.observe(this) { oneTapStatus ->
            if (oneTapStatus.isValid) {
                authViewModel.verifyIfProfileIsCreated()
            } else {
                FrequencyFunctions.makeToast(this, oneTapStatus.message)
            }
        }
        authViewModel.isProfileCreated.observe(this){isProfileCreated ->
            if(isProfileCreated){
                FrequencyFunctions.startNewActivityFromCurrentActivity(this, MainActivity())
            }else{
                FrequencyFunctions.startNewActivityFromCurrentActivity(
                    this,
                    CreateProfileActivity()
                )
            }
        }

    }
}