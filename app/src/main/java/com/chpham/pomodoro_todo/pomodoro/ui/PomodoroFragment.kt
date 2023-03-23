package com.chpham.pomodoro_todo.pomodoro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.chpham.domain.timer.CountDownTimerState
import com.chpham.pomodoro_todo.HomeActivity
import com.chpham.pomodoro_todo.R
import com.chpham.pomodoro_todo.base.ui.BaseFragment
import com.chpham.pomodoro_todo.base.viewmodel.ViewModelFactory
import com.chpham.pomodoro_todo.databinding.FragmentPomodoroBinding
import com.chpham.pomodoro_todo.pomodoro.viewmodel.PomodoroViewModel
import com.chpham.pomodoro_todo.utils.PomodoroMode
import com.chpham.pomodoro_todo.utils.changeBackground
import com.chpham.pomodoro_todo.utils.convertMinutesToSeconds
import com.chpham.pomodoro_todo.utils.convertSecondsToMinutes
import com.chpham.pomodoro_todo.utils.getFormattedMinutes
import com.chpham.pomodoro_todo.utils.rotateView

class PomodoroFragment : BaseFragment<FragmentPomodoroBinding>(), View.OnClickListener {

    private var currentMode = PomodoroMode.MODE_POMODORO

    private lateinit var viewModel: PomodoroViewModel

    private var currentTimerState: CountDownTimerState = CountDownTimerState.NotStarted

    /**
     * A map that maps button IDs to their respective mode values.
     * The map is used to determine the mode associated with a clicked button.
     * The keys are the IDs of the buttons, and the values are the corresponding mode values.
     */
    private val buttonModeMap = mapOf(
        R.id.btnPomodoro to PomodoroMode.MODE_POMODORO,
        R.id.btnShortBreak to PomodoroMode.MODE_SHORT_BREAK,
        R.id.btnLongBreak to PomodoroMode.MODE_LONG_BREAK
    )

    private var isInFocus: Boolean? = null

    companion object {
        fun newInstance(): PomodoroFragment {
            return PomodoroFragment()
        }
    }

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        return FragmentPomodoroBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        setUpBackground()
        setUpOnClickListener()
        observeData(this.viewLifecycleOwner)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(requireContext())
        )[PomodoroViewModel::class.java]
    }

    private fun observeData(lifecycleOwner: LifecycleOwner) {
        viewModel.countDownTimerState.observe(lifecycleOwner) { state ->
            when (state) {
                is CountDownTimerState.Started -> {
                    currentTimerState = state
                    binding.btnStart.text = getString(R.string.text_btn_pause)
                }
                is CountDownTimerState.Tick -> {
                    if (currentTimerState != CountDownTimerState.Paused &&
                        currentTimerState != CountDownTimerState.Finished
                    ) {
                        binding.tvTimer.text = state.currentCount.convertSecondsToMinutes()
                        currentTimerState = CountDownTimerState.Counting
                    }
                }
                is CountDownTimerState.Finished -> {
                    binding.tvTimer.text = currentMode.minutes.getFormattedMinutes()
                    binding.btnStart.text = getString(R.string.text_btn_start)
                    currentTimerState = CountDownTimerState.Finished
                }
                else -> {}
            }
        }
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
                            binding.root.findViewById<Button>(
                                buttonId
                            ).changeBackground(false)
                        }
                    }
                    (clickedButton as Button).changeBackground(true)
                }
            }
            R.id.btnStart -> {
                when (currentTimerState) {
                    CountDownTimerState.NotStarted -> {
                        viewModel.startCountDownTimer(currentMode.minutes.convertMinutesToSeconds())
                        turnOnFocus()
                    }
                    CountDownTimerState.Started -> {
                        viewModel.restartCountDownTimer(
                            currentMode.minutes.convertMinutesToSeconds()
                        )
                        binding.btnStart.text = getString(R.string.text_btn_pause)
                        viewModel.resumeCountDownTimer()
                        currentTimerState = CountDownTimerState.Counting
                        turnOnFocus()
                    }
                    CountDownTimerState.Finished -> {
                        viewModel.startCountDownTimer(
                            currentMode.minutes.convertMinutesToSeconds()
                        )
                        binding.btnStart.text = getString(R.string.text_btn_pause)
                        currentTimerState = CountDownTimerState.Counting
                        turnOffFocus()
                    }
                    CountDownTimerState.Counting -> {
                        viewModel.pauseCountDownTimer()
                        currentTimerState = CountDownTimerState.Paused
                        binding.btnStart.text = getString(R.string.text_btn_resume)
                        turnOffFocus()
                    }
                    CountDownTimerState.Paused -> {
                        viewModel.resumeCountDownTimer()
                        currentTimerState = CountDownTimerState.Counting
                        binding.btnStart.text = getString(R.string.text_btn_pause)
                        turnOnFocus()
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
            R.id.btnRestart -> {
                if (isInFocus == true) {
                    turnOffFocus()
                }
                binding.btnRestart.rotateView()
                if (currentTimerState != CountDownTimerState.NotStarted) {
                    binding.tvTimer.text = currentMode.minutes.getFormattedMinutes()
                    binding.btnStart.text = getString(R.string.text_btn_start)
                }
                viewModel.restartCountDownTimer(currentMode.minutes.convertMinutesToSeconds())
                currentTimerState = CountDownTimerState.Started
            }
        }
    }

    /**
     * This function changes the current mode of the timer and updates the UI accordingly.
     */
    private fun changeMode(newMode: PomodoroMode) {
        currentMode = newMode
        binding.tvTimer.text = currentMode.minutes.getFormattedMinutes()
        binding.btnStart.text = getString(R.string.text_btn_start)
        if (currentTimerState == CountDownTimerState.Counting ||
            currentTimerState == CountDownTimerState.Paused
        ) {
            viewModel.pauseCountDownTimer()
            currentTimerState = CountDownTimerState.Started
        }

        turnOffFocus()
    }

    private fun turnOnFocus() {
        (activity as? HomeActivity)?.setFocusStatus(
            shouldFocusing = true
        )
        isInFocus = true
    }

    private fun turnOffFocus() {
        (activity as? HomeActivity)?.setFocusStatus(
            shouldFocusing = false
        )
        isInFocus = false
    }

    override fun onResume() {
        super.onResume()
        isInFocus?.let {
            if (it) {
                turnOnFocus()
            } else {
                turnOffFocus()
            }
        }
    }
}
