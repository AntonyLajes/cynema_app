package com.nomargin.cynema.ui.activity.main_activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nomargin.cynema.R
import com.nomargin.cynema.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val bottomNavigationView: BottomNavigationView by lazy { binding.bottomNavigationView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bottomNavigationListenerHandler()
    }

    private fun bottomNavigationListenerHandler() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            navigationFragmentHandler(item)
        }
        bottomNavigationView.setOnItemReselectedListener { item ->
            navigationFragmentHandler(item)
        }
    }

    private fun navigationFragmentHandler(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home_fragment -> {
                findNavController(binding.fragmentContainerView.id).navigate(R.id.home_fragment)
                true
            }
            R.id.search_fragment -> {
                findNavController(binding.fragmentContainerView.id).navigate(R.id.search_fragment)
                true
            }
            R.id.favorites_fragment -> {
                findNavController(binding.fragmentContainerView.id).navigate(R.id.favorites_fragment)
                true
            }
            R.id.profile_fragment -> {
                findNavController(binding.fragmentContainerView.id).navigate(R.id.profile_fragment)
                true
            }
            else -> false
        }
    }
}