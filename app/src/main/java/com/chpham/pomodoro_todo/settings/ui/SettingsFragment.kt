package com.chpham.pomodoro_todo.settings.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chpham.pomodoro_todo.base.ui.BaseFragment
import com.chpham.pomodoro_todo.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    companion object {

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        return FragmentSettingsBinding.inflate(layoutInflater, container, false)
    }
}
