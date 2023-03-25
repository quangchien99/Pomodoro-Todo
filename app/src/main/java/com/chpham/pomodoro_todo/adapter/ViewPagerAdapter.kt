package com.chpham.pomodoro_todo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Adapter class for the ViewPager2 in MainActivity. Manages a list of Fragments
 * to be displayed in the ViewPager2.
 *
 * @param activity The fragment activity that owns the ViewPager2.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    /** List of fragments to be displayed in the ViewPager2. */
    private var fragmentList: MutableList<Fragment> = arrayListOf()

    /**
     * Returns the number of fragments in the ViewPager2.
     */
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    /**
     * Creates and returns a new Fragment for the specified position in the ViewPager2.
     *
     * @param position The position of the Fragment in the ViewPager2.
     * @return The Fragment at the specified position.
     */
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    /**
     * Adds a Fragment to the list of fragments managed by the adapter.
     *
     * @param fragment The Fragment to be added.
     */
    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }
}
