package com.chpham.pomodoro_todo.todo.ui

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.chpham.domain.model.RemindOptions
import com.chpham.pomodoro_todo.R
import com.chpham.pomodoro_todo.databinding.LayoutRepeatOptionsBinding
import com.chpham.pomodoro_todo.utils.createAlertDialog

class RepeatOptionsDialog(
    private val context: Context,
    private val layoutRepeatOptionsBinding: LayoutRepeatOptionsBinding
) {

    private var selectedMode: RemindOptions.RemindMode
    private var selectedInterval: String?
    private var selectedRepeatIn: String?
    private var selectedEndInt: String?

    private var tempMode: RemindOptions.RemindMode?
    private var temptInterval: String?
    private var tempRepeatIn: String?
    private var tempEndInt: String?

    private val intervalDailyValues: Array<String>
    private val intervalWeeklyValues: Array<String>
    private val intervalMonthlyValues: Array<String>
    private val endDailyValues: Array<String>
    private val endWeeklyValues: Array<String>
    private val endMonthlyValues: Array<String>
    private val repeatMonthlyValues: Array<String>

    private val adapterIntervalDaily: ArrayAdapter<CharSequence>
    private val adapterIntervalWeekly: ArrayAdapter<CharSequence>
    private val adapterIntervalMonthly: ArrayAdapter<CharSequence>
    private val adapterEndDaily: ArrayAdapter<CharSequence>
    private val adapterEndWeekly: ArrayAdapter<CharSequence>
    private val adapterEndMonthly: ArrayAdapter<CharSequence>
    private val adapterRepeatInMonthly: ArrayAdapter<CharSequence>

    private val setReminderAlertDialog: AlertDialog

    private lateinit var repeatResultHandler: (RemindOptions.RemindMode, String?, String?, String?) -> Unit

    init {
        selectedMode = RemindOptions.RemindMode.UN_SPECIFIED
        selectedInterval = null
        selectedRepeatIn = null
        selectedEndInt = null

        tempMode = RemindOptions.RemindMode.UN_SPECIFIED
        temptInterval = null
        tempRepeatIn = null
        tempEndInt = null

        intervalDailyValues = context.resources.getStringArray(R.array.interval_daily)
        intervalWeeklyValues = context.resources.getStringArray(R.array.interval_weekly)
        intervalMonthlyValues = context.resources.getStringArray(R.array.interval_monthly)
        endDailyValues = context.resources.getStringArray(R.array.end_daily)
        endWeeklyValues = context.resources.getStringArray(R.array.end_weekly)
        endMonthlyValues = context.resources.getStringArray(R.array.end_monthly)
        repeatMonthlyValues = context.resources.getStringArray(R.array.repeat_in_monthly)

        adapterIntervalDaily = createSpinnerAdapter(context, R.array.interval_daily)
        adapterIntervalWeekly = createSpinnerAdapter(context, R.array.interval_weekly)
        adapterIntervalMonthly = createSpinnerAdapter(context, R.array.interval_monthly)
        adapterEndDaily = createSpinnerAdapter(context, R.array.end_daily)
        adapterEndWeekly = createSpinnerAdapter(context, R.array.end_weekly)
        adapterEndMonthly = createSpinnerAdapter(context, R.array.end_monthly)
        adapterRepeatInMonthly = createSpinnerAdapter(context, R.array.repeat_in_monthly)

        setReminderAlertDialog = layoutRepeatOptionsBinding.createAlertDialog(context)

        setUpListener()
    }

    private fun setUpListener() {
        layoutRepeatOptionsBinding.spinnerRepeatEvery.onItemSelectedListener =
            createItemSelectedListener { selectedItem ->
                temptInterval = selectedItem
            }

        layoutRepeatOptionsBinding.spinnerRepeatEnd.onItemSelectedListener =
            createItemSelectedListener { selectedItem ->
                tempEndInt = selectedItem
            }

        layoutRepeatOptionsBinding.spinnerRepeatIn.onItemSelectedListener =
            createItemSelectedListener { selectedItem ->
                tempRepeatIn = selectedItem
            }

        layoutRepeatOptionsBinding.tvDaily.setOnClickListener {
            setRepeatOptions(
                RemindOptions.RemindMode.DAILY, adapterIntervalDaily, adapterEndDaily, null
            )
        }

        layoutRepeatOptionsBinding.tvWeekly.setOnClickListener {
            setRepeatOptions(
                RemindOptions.RemindMode.WEEKLY, adapterIntervalWeekly, adapterEndWeekly, null
            )
        }

        layoutRepeatOptionsBinding.tvMonthly.setOnClickListener {
            setRepeatOptions(
                RemindOptions.RemindMode.MONTHLY,
                adapterIntervalMonthly,
                adapterEndMonthly,
                adapterRepeatInMonthly
            )
        }

        layoutRepeatOptionsBinding.switchRepeat.setOnClickListener {
            if (layoutRepeatOptionsBinding.switchRepeat.isChecked) {
                layoutRepeatOptionsBinding.tvDaily.performClick()
            } else {
                unSelectPreviousMode(tempMode ?: return@setOnClickListener)
                layoutRepeatOptionsBinding.layoutRepeatEvery.visibility = View.GONE
                layoutRepeatOptionsBinding.layoutRepeatInWeek.visibility = View.GONE
                layoutRepeatOptionsBinding.layoutRepeatInMonth.visibility = View.GONE
                layoutRepeatOptionsBinding.layoutRepeatEndIn.visibility = View.GONE
            }
        }

        layoutRepeatOptionsBinding.btnCancel.setOnClickListener {
            setReminderAlertDialog.dismiss()
        }

        layoutRepeatOptionsBinding.btnConfirm.setOnClickListener {
            tempMode?.let {
                selectedMode = it
                selectedInterval = temptInterval
                selectedRepeatIn = tempRepeatIn
                selectedEndInt = tempEndInt
            }
            setReminderAlertDialog.dismiss()
        }

        setReminderAlertDialog.setOnDismissListener {
            repeatResultHandler.invoke(
                selectedMode,
                selectedInterval,
                selectedRepeatIn,
                selectedEndInt
            )
        }
    }

    fun handleSelectRepetition(
        currentMode: RemindOptions.RemindMode = RemindOptions.RemindMode.UN_SPECIFIED,
        currentInterval: String? = null,
        currentRepeatIn: String? = null,
        currentEndInt: String? = null,
        repeatResultHandler: (RemindOptions.RemindMode, String?, String?, String?) -> Unit
    ) {
        this.repeatResultHandler = repeatResultHandler

        handlePreviousData(
            currentMode = currentMode,
            currentInterval = currentInterval,
            currentRepeatIn = currentRepeatIn,
            currentEndInt = currentEndInt
        )

        setReminderAlertDialog.show()
    }

    private fun handlePreviousData(
        currentMode: RemindOptions.RemindMode = RemindOptions.RemindMode.UN_SPECIFIED,
        currentInterval: String? = null,
        currentRepeatIn: String? = null,
        currentEndInt: String? = null
    ) {
        when (currentMode) {
            RemindOptions.RemindMode.DAILY -> {
                layoutRepeatOptionsBinding.tvDaily.performClick()

                currentInterval?.let {
                    layoutRepeatOptionsBinding.spinnerRepeatEvery.setSelection(
                        intervalDailyValues.indexOf(it)
                    )
                }

                currentEndInt?.let {
                    layoutRepeatOptionsBinding.spinnerRepeatEnd.setSelection(
                        endDailyValues.indexOf(it)
                    )
                }
            }
            RemindOptions.RemindMode.WEEKLY -> {
                layoutRepeatOptionsBinding.tvWeekly.performClick()
                currentInterval?.let {
                    layoutRepeatOptionsBinding.spinnerRepeatEvery.setSelection(
                        intervalWeeklyValues.indexOf(it)
                    )
                }

                currentEndInt?.let {
                    layoutRepeatOptionsBinding.spinnerRepeatEnd.setSelection(
                        endWeeklyValues.indexOf(it)
                    )
                }

                when (currentRepeatIn) {
                    context.getString(R.string.text_mon) -> {
                        layoutRepeatOptionsBinding.tvMonday.performClick()
                    }
                    context.getString(R.string.text_tue) -> {
                        layoutRepeatOptionsBinding.tvTuesday.performClick()
                    }
                    context.getString(R.string.text_wed) -> {
                        layoutRepeatOptionsBinding.tvWednesday.performClick()
                    }
                    context.getString(R.string.text_thu) -> {
                        layoutRepeatOptionsBinding.tvThursday.performClick()
                    }
                    context.getString(R.string.text_fri) -> {
                        layoutRepeatOptionsBinding.tvFriday.performClick()
                    }
                    context.getString(R.string.text_sat) -> {
                        layoutRepeatOptionsBinding.tvSaturday.performClick()
                    }
                    context.getString(R.string.text_sun) -> {
                        layoutRepeatOptionsBinding.tvSunday.performClick()
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
            RemindOptions.RemindMode.MONTHLY -> {
                layoutRepeatOptionsBinding.tvMonthly.performClick()
                currentInterval?.let {
                    layoutRepeatOptionsBinding.spinnerRepeatEvery.setSelection(
                        intervalMonthlyValues.indexOf(it)
                    )
                }

                currentRepeatIn?.let {
                    layoutRepeatOptionsBinding.spinnerRepeatIn.setSelection(
                        repeatMonthlyValues.indexOf(it)
                    )
                }

                currentEndInt?.let {
                    layoutRepeatOptionsBinding.spinnerRepeatEnd.setSelection(
                        endMonthlyValues.indexOf(it)
                    )
                }
            }
            else -> {
                // continue
            }
        }
    }

    private fun setRepeatOptions(
        mode: RemindOptions.RemindMode,
        intervalAdapter: ArrayAdapter<CharSequence>,
        endAdapter: ArrayAdapter<CharSequence>,
        inAdapter: ArrayAdapter<CharSequence>?
    ) {
        unSelectPreviousMode(tempMode ?: return)

        layoutRepeatOptionsBinding.switchRepeat.isChecked = true

        layoutRepeatOptionsBinding.layoutRepeatEvery.visibility = View.VISIBLE
        layoutRepeatOptionsBinding.spinnerRepeatEvery.adapter = intervalAdapter

        layoutRepeatOptionsBinding.layoutRepeatEndIn.visibility = View.VISIBLE
        layoutRepeatOptionsBinding.spinnerRepeatEnd.adapter = endAdapter

        inAdapter?.let {
            layoutRepeatOptionsBinding.layoutRepeatInMonth.visibility = View.VISIBLE
            layoutRepeatOptionsBinding.spinnerRepeatIn.adapter = it
        } ?: run {
            layoutRepeatOptionsBinding.layoutRepeatInMonth.visibility = View.GONE
        }

        layoutRepeatOptionsBinding.layoutRepeatInWeek.visibility = View.GONE

        tempMode = mode
        when (mode) {
            RemindOptions.RemindMode.DAILY -> layoutRepeatOptionsBinding.tvDaily.setBackgroundResource(
                R.drawable.bg_btn_date_selected
            )
            RemindOptions.RemindMode.WEEKLY -> {
                layoutRepeatOptionsBinding.tvWeekly.setBackgroundResource(R.drawable.bg_btn_date_selected)

                layoutRepeatOptionsBinding.layoutRepeatInWeek.visibility = View.VISIBLE

                val tvMappings = mapOf(
                    context.getString(R.string.text_mon) to layoutRepeatOptionsBinding.tvMonday,
                    context.getString(R.string.text_tue) to layoutRepeatOptionsBinding.tvTuesday,
                    context.getString(R.string.text_wed) to layoutRepeatOptionsBinding.tvWednesday,
                    context.getString(R.string.text_thu) to layoutRepeatOptionsBinding.tvThursday,
                    context.getString(R.string.text_fri) to layoutRepeatOptionsBinding.tvFriday,
                    context.getString(R.string.text_sat) to layoutRepeatOptionsBinding.tvSaturday,
                    context.getString(R.string.text_sun) to layoutRepeatOptionsBinding.tvSunday
                )

                val tvDaysInWeek = listOf(
                    layoutRepeatOptionsBinding.tvMonday,
                    layoutRepeatOptionsBinding.tvTuesday,
                    layoutRepeatOptionsBinding.tvWednesday,
                    layoutRepeatOptionsBinding.tvThursday,
                    layoutRepeatOptionsBinding.tvFriday,
                    layoutRepeatOptionsBinding.tvSaturday,
                    layoutRepeatOptionsBinding.tvSunday
                )

                for (tvDay in tvDaysInWeek) {
                    tvDay.setOnClickListener {
                        updateStateItemDayOfWeek(tvMappings)
                        tempRepeatIn = tvDay.text.toString()
                        tvDay.setBackgroundResource(R.drawable.bg_btn_date_selected)
                    }
                }
            }
            RemindOptions.RemindMode.MONTHLY -> layoutRepeatOptionsBinding.tvMonthly.setBackgroundResource(
                R.drawable.bg_btn_date_selected
            )
            else -> {
                // do nothing
            }
        }
    }

    private fun unSelectPreviousMode(mode: RemindOptions.RemindMode) {
        when (mode) {
            RemindOptions.RemindMode.DAILY -> {
                layoutRepeatOptionsBinding.tvDaily.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
            RemindOptions.RemindMode.WEEKLY -> {
                layoutRepeatOptionsBinding.tvWeekly.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
            RemindOptions.RemindMode.MONTHLY -> {
                layoutRepeatOptionsBinding.tvMonthly.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
            else -> {
                // do nothing
            }
        }
    }

    private fun createItemSelectedListener(itemCallback: (String) -> Unit): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(pos).toString()
                itemCallback(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing here
            }
        }
    }

    private fun updateStateItemDayOfWeek(tvMappings: Map<String, TextView>) {
        tvMappings[tempRepeatIn]?.setBackgroundResource(R.drawable.bg_btn_date_unselected)
    }

    private fun createSpinnerAdapter(cxt: Context, arrayResId: Int): ArrayAdapter<CharSequence> {
        val adapter = ArrayAdapter.createFromResource(
            cxt, arrayResId, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }
}
