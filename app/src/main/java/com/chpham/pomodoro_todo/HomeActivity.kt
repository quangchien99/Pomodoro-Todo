package com.chpham.pomodoro_todo

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.chpham.pomodoro_todo.base.ui.BaseActivity
import com.chpham.pomodoro_todo.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity<ActivityHomeBinding>(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpBottomNav()
    }

    override fun initViewBinding(): ViewBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    private fun setUpBottomNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(
            navController
        )

    }

}