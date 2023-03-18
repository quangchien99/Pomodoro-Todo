package com.chpham.pomodoro_todo.todo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chpham.pomodoro_todo.base.ui.BaseFragment
import com.chpham.pomodoro_todo.databinding.FragmentTodoBinding

class TodoListFragment : BaseFragment<FragmentTodoBinding>() {

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        return FragmentTodoBinding.inflate(layoutInflater, container, false)
    }

}