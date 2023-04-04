package com.chpham.pomodoro_todo

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.chpham.pomodoro_todo.adapter.ViewPagerAdapter
import com.chpham.pomodoro_todo.base.ui.BaseActivity
import com.chpham.pomodoro_todo.databinding.ActivityHomeBinding
import com.chpham.pomodoro_todo.pomodoro.ui.PomodoroFragment
import com.chpham.pomodoro_todo.todo.ui.TodoListFragment
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    companion object {
        private const val FRAGMENT_POMODORO_INDEX = 0
        private const val FRAGMENT_TODO_INDEX = 1
        private const val SLIDE_ANIMATION_DURATION = 500L
    }

    private val onItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.itemOptions -> {
                binding.layoutDrawer.openDrawer(GravityCompat.START)
                /**
                 * Not set it selected, just open the left drawer
                 */
                return@OnItemSelectedListener false
            }
            R.id.itemPomodoro -> {
                binding.viewPager.currentItem = FRAGMENT_POMODORO_INDEX
                return@OnItemSelectedListener true
            }
            R.id.itemTodoList -> {
                binding.viewPager.currentItem = FRAGMENT_TODO_INDEX
                return@OnItemSelectedListener true
            }
        }
        false
    }

    private var isFocusingOn = false

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
        viewPager.apply {
            adapter = viewPagerAdapter
        }
    }

    private fun setUpPageChangesListener() {
        var itemMenu: MenuItem = binding.bottomNavigation.menu.getItem(FRAGMENT_POMODORO_INDEX)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                /**
                 * Show the bottomNavigation when moving to fragment to do
                 */
                if ((position == FRAGMENT_TODO_INDEX) &&
                    isFocusingOn
                ) {
                    setFocusStatus(false)
                }

                itemMenu.isChecked = false
                /**
                 * Increased by 1 because we will skip selected the first item of menu - option (left drawer)
                 */
                binding.bottomNavigation.menu.getItem(position + 1).isChecked = true
                itemMenu = binding.bottomNavigation.menu.getItem(position + 1)
            }
        }
        )
    }

    fun setFocusStatus(shouldFocusing: Boolean = false) {
        if (!shouldFocusing) {
            binding.bottomNavigation.animate().translationY(0f).duration =
                SLIDE_ANIMATION_DURATION
        } else {
            val scale = resources.displayMetrics.density
            val height = (60 * scale + 0.5f)
            binding.bottomNavigation.animate().translationY(height).duration =
                SLIDE_ANIMATION_DURATION
        }
        isFocusingOn = shouldFocusing
    }
}
