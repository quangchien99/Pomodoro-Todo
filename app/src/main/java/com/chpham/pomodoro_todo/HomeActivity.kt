package com.chpham.pomodoro_todo

import android.os.Bundle
import android.view.MenuItem
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.chpham.pomodoro_todo.adapter.ViewPagerAdapter
import com.chpham.pomodoro_todo.base.ui.BaseActivity
import com.chpham.pomodoro_todo.databinding.ActivityHomeBinding
import com.chpham.pomodoro_todo.pomodoro.ui.PomodoroFragment
import com.chpham.pomodoro_todo.settings.ui.SettingsFragment
import com.chpham.pomodoro_todo.todo.ui.TodoListFragment
import com.google.android.material.navigation.NavigationBarView

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    companion object {
        private const val FRAGMENT_POMODORO_INDEX = 0
        private const val FRAGMENT_TODO_INDEX = 1
        private const val FRAGMENT_SETTINGS_INDEX = 2
    }

    private val onItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.itemPomodoro -> {
                binding.viewPager.currentItem = FRAGMENT_POMODORO_INDEX
                return@OnItemSelectedListener true
            }
            R.id.itemTodoList -> {
                binding.viewPager.currentItem = FRAGMENT_TODO_INDEX
                return@OnItemSelectedListener true
            }
            R.id.itemSettings -> {
                binding.viewPager.currentItem = FRAGMENT_SETTINGS_INDEX
                return@OnItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpBottomNav()
        setUpViewPager(binding.viewPager)
        setUpPageChangesListener()
    }

    override fun initViewBinding(): ViewBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    private fun setUpBottomNav() {
        binding.bottomNavigation.setOnItemSelectedListener(onItemSelectedListener)
    }

    private fun setUpViewPager(viewPager: ViewPager2) {
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPagerAdapter.addFragment(PomodoroFragment.newInstance())
        viewPagerAdapter.addFragment(TodoListFragment.newInstance())
        viewPagerAdapter.addFragment(SettingsFragment.newInstance())
        viewPager.apply {
            adapter = viewPagerAdapter
        }
    }

    private fun setUpPageChangesListener() {
        var itemMenu: MenuItem = binding.bottomNavigation.menu.getItem(FRAGMENT_POMODORO_INDEX)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                itemMenu.isChecked = false
                binding.bottomNavigation.menu.getItem(position).isChecked = true
                itemMenu = binding.bottomNavigation.menu.getItem(position)
            }
        }
        )
    }

}