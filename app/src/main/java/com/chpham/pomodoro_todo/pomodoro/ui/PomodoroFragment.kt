package com.chpham.pomodoro_todo.pomodoro.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chpham.pomodoro_todo.base.ui.BaseFragment
import com.chpham.pomodoro_todo.databinding.FragmentPomodoroBinding

class PomodoroFragment : BaseFragment<FragmentPomodoroBinding>() {

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding? {
        return FragmentPomodoroBinding.inflate(layoutInflater, container, false)
    }

}