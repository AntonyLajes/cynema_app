package com.nomargin.cynema_app.ui.activity.auth_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nomargin.cynema_app.R
import com.nomargin.cynema_app.databinding.ActivityAuthBinding
import com.nomargin.cynema_app.ui.adapter.view_pager.ViewPagerAdapter
import com.nomargin.cynema_app.ui.fragment.sign_in_fragment.SignInFragment
import com.nomargin.cynema_app.ui.fragment.sign_up_fragment.SignUpFragment

class AuthActivity : AppCompatActivity() {

    private val binding: ActivityAuthBinding by lazy { ActivityAuthBinding.inflate(layoutInflater) }
    private val viewPagerAdapter: ViewPagerAdapter by lazy { ViewPagerAdapter(this) }
    private val tabLayout: TabLayout by lazy { binding.tabLayout }
    private val viewPager: ViewPager2 by lazy { binding.viewPager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewPagerInflater()
    }

    private fun viewPagerInflater() {
        viewPager.adapter = viewPagerAdapter
        viewPagerAdapter.setFragment(SignInFragment(), getString(R.string.sign_in_fragment_name))
        viewPagerAdapter.setFragment(SignUpFragment(), getString(R.string.sign_up_fragment_name))
        viewPager.offscreenPageLimit = viewPagerAdapter.itemCount
        val mediator = TabLayoutMediator(tabLayout, viewPager){tab, position ->
            tab.text = viewPagerAdapter.getTitle(position)
        }
        mediator.attach()
    }
}