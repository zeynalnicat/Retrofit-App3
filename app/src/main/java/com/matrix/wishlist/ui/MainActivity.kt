package com.matrix.wishlist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.matrix.wishlist.R
import com.matrix.wishlist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navHostFragment.navController
        )

        val navHostFragmentDrawer =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.navigationView,
            navHostFragmentDrawer.navController
        )

        binding.floatingActionButton2.setOnClickListener{
            if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
                binding.drawer.closeDrawer(GravityCompat.START)
            } else {
                binding.drawer.openDrawer(GravityCompat.START)
            }
        }

        setContentView(binding.root)
    }

    fun setBottomNavigation(visibility: Boolean) {
        binding.bottomNavigationView.visibility = if (visibility) View.VISIBLE else View.GONE
        binding.navigationView.visibility = if (visibility) View.VISIBLE else View.GONE
        binding.floatingActionButton2.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

    }


}