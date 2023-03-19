package com.chpham.pomodoro_todo.pomodoro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.chpham.pomodoro_todo.R
import com.chpham.pomodoro_todo.base.ui.BaseFragment
import com.chpham.pomodoro_todo.databinding.FragmentPomodoroBinding
import com.chpham.pomodoro_todo.utils.changeBackground
import com.chpham.pomodoro_todo.utils.rotateView

class PomodoroFragment : BaseFragment<FragmentPomodoroBinding>(), View.OnClickListener {

    private var currentMode = MODE_POMODORO

    /**
    A map that maps button IDs to their respective mode values.
    The map is used to determine the mode associated with a clicked button.
    The keys are the IDs of the buttons, and the values are the corresponding mode values.
     */
    private val buttonModeMap = mapOf(
        R.id.btnPomodoro to MODE_POMODORO,
        R.id.btnShortBreak to MODE_SHORT_BREAK,
        R.id.btnLongBreak to MODE_LONG_BREAK
    )

    companion object {
        fun newInstance(): PomodoroFragment {
            return PomodoroFragment()
        }

        private const val MODE_POMODORO = 0
        private const val MODE_SHORT_BREAK = 1
        private const val MODE_LONG_BREAK = 2
    }

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        return FragmentPomodoroBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBackground()
        setUpOnClickListener()
    }

    private fun setUpBackground() {
        binding.imgBackground.apply {
            Glide.with(context)
                .load(R.drawable.rainy)
                .into(this)
        }
    }

    private fun setUpOnClickListener() {
        binding.btnPomodoro.setOnClickListener(this)
        binding.btnShortBreak.setOnClickListener(this)
        binding.btnLongBreak.setOnClickListener(this)
        binding.btnStart.setOnClickListener(this)
        binding.btnRestart.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnPomodoro,
            R.id.btnShortBreak,
            R.id.btnLongBreak -> {
                view.let { clickedButton ->
                    val clickedButtonMode = buttonModeMap[clickedButton.id] ?: return
                    if (clickedButtonMode == currentMode) {
                        return
                    }
                    changeMode(clickedButtonMode)
                    buttonModeMap.forEach { (buttonId, mode) ->
                        if (mode != clickedButtonMode) {
                            binding.root.findViewById<Button>(buttonId).changeBackground(false)
                        }
                    }
                    (clickedButton as Button).changeBackground(true)
                }
            }
            R.id.btnStart -> {

            }
            R.id.btnRestart -> {
                binding.btnRestart.rotateView()
            }
        }
    }

    /**
    This function changes the current mode of the timer and updates the UI accordingly.
     */
    private fun changeMode(newMode: Int) {
        currentMode = newMode
        when (currentMode) {
            MODE_POMODORO -> {
                binding.tvTimer.text = getString(R.string.text_timer_25)
            }
            MODE_SHORT_BREAK -> {
                binding.tvTimer.text = getString(R.string.text_timer_5)
            }
            MODE_LONG_BREAK -> {
                binding.tvTimer.text = getString(R.string.text_timer_10)
            }
        }
    }
}