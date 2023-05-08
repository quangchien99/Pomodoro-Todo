package com.chpham.data.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.chpham.domain.R
import com.chpham.domain.model.RemindOptions
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val MILLIS_A_DAY = 86400000L
        private const val INVALID_INT = -1
        private const val INVALID_LONG = -1L
        private const val ZERO_VALUE = 0
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        if (context == null) {
            return
        }

        val message = intent?.getStringExtra(AlarmSchedulerImpl.MESSAGE) ?: return

        val id = intent.getIntExtra(AlarmSchedulerImpl.ID, INVALID_INT)
        if (id == INVALID_INT) {
            return
        }

        val startDate = intent.getLongExtra(AlarmSchedulerImpl.START_DATE, INVALID_LONG)
        if (startDate == INVALID_LONG) {
            return
        }

        val timeRemind = intent.getLongExtra(AlarmSchedulerImpl.TIME_REMIND, INVALID_LONG)
        if (timeRemind == INVALID_LONG) {
            return
        }

        val remindOptions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(AlarmSchedulerImpl.REMIND_OPTION, RemindOptions::class.java)
        } else {
            intent.getParcelableExtra(AlarmSchedulerImpl.REMIND_OPTION)
        }

        val isUpdate = intent.getBooleanExtra(AlarmSchedulerImpl.IS_UPDATE, false)

        if (!isUpdate) {
            showNotification(context, message)
        }

        val todayDate = Calendar.getInstance().apply {
            set(Calendar.MINUTE, ZERO_VALUE)
            set(Calendar.SECOND, ZERO_VALUE)
            set(Calendar.MILLISECOND, ZERO_VALUE)
        }
        Log.e("ChienNgan", "today Date= ${todayDate.timeInMillis}")
        Log.e("ChienNgan", "onReceive: startDate= $startDate, remind = $remindOptions")
        remindOptions?.let { remind ->
            if (remind.mode == RemindOptions.RemindMode.UN_SPECIFIED) {
                return
            } else {
                val alarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

                when (remind.mode) {
                    RemindOptions.RemindMode.DAILY -> {
                        val nextRemindTime =
                            todayDate.timeInMillis + (remind.interval * MILLIS_A_DAY)
                        if (nextRemindTime - startDate > remind.endInt * MILLIS_A_DAY) {
                            return
                        }

                        val timeHhSs = timeHhSs(timeRemind)
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = nextRemindTime
                        calendar.apply {
                            set(Calendar.HOUR_OF_DAY, timeHhSs.split(":")[0].toInt())
                            set(Calendar.MINUTE, timeHhSs.split(":")[1].toInt())
                            set(Calendar.SECOND, ZERO_VALUE)
                        }
                        Log.e(
                            "ChienNgan",
                            "DAILY - next remind is ${formatTime(calendar.timeInMillis)}"
                        )
                        alarmManager?.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            PendingIntent.getBroadcast(
                                context,
                                id,
                                getIntent(
                                    context,
                                    id,
                                    message,
                                    startDate,
                                    timeRemind,
                                    remindOptions
                                ),
                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                            ),
                        )
                    }
                    RemindOptions.RemindMode.WEEKLY -> {
                        if (remind.repeatInWeek.isEmpty()) {
                            val nextRemindTime =
                                todayDate.timeInMillis + (remind.interval * MILLIS_A_DAY)
                            if (nextRemindTime - startDate > remind.endInt * MILLIS_A_DAY) {
                                return
                            }
                            val timeHhSs = timeHhSs(timeRemind)
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = nextRemindTime
                            calendar.apply {
                                set(Calendar.HOUR_OF_DAY, timeHhSs.split(":")[0].toInt())
                                set(Calendar.MINUTE, timeHhSs.split(":")[1].toInt())
                                set(Calendar.SECOND, ZERO_VALUE)
                            }
                            Log.e(
                                "ChienNgan",
                                "Weekly repeatInWeek empty - next remind is " + formatTime(
                                    calendar.timeInMillis
                                )
                            )
                            alarmManager?.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis,
                                PendingIntent.getBroadcast(
                                    context,
                                    id,
                                    getIntent(
                                        context,
                                        id,
                                        message,
                                        startDate,
                                        timeRemind,
                                        remindOptions
                                    ),
                                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                                ),
                            )
                        } else {
                            val calendar =
                                getNextNearestDayMillis(remind.repeatInWeek, remind.interval)
                                    ?: return

                            if (calendar.timeInMillis - startDate > remind.endInt * MILLIS_A_DAY) {
                                return
                            }

                            val timeHhSs = timeHhSs(timeRemind)
                            calendar.apply {
                                set(Calendar.HOUR_OF_DAY, timeHhSs.split(":")[0].toInt())
                                set(Calendar.MINUTE, timeHhSs.split(":")[1].toInt())
                                set(Calendar.SECOND, ZERO_VALUE)
                            }
                            Log.e(
                                "ChienNgan",
                                "Weekly - next remind is ${formatTime(calendar.timeInMillis)}"
                            )
                            alarmManager?.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis,
                                PendingIntent.getBroadcast(
                                    context,
                                    id,
                                    getIntent(
                                        context,
                                        id,
                                        message,
                                        startDate,
                                        timeRemind,
                                        remindOptions
                                    ),
                                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                                ),
                            )
                        }
                    }
                    RemindOptions.RemindMode.MONTHLY -> {
                        val nextRemindTime =
                            todayDate.timeInMillis + (remind.interval * MILLIS_A_DAY)
                        if (nextRemindTime - startDate > remind.endInt * MILLIS_A_DAY) {
                            return
                        }
                        val nextRemindDay = getTimeInMillisForDayOfMonth(
                            remind.repeatInMonth,
                            (remind.interval / 30).toLong()
                        )
                        if (nextRemindDay - startDate > remind.endInt * MILLIS_A_DAY) {
                            return
                        } else {
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = nextRemindDay
                            val timeHhSs = timeHhSs(timeRemind)
                            calendar.apply {
                                set(Calendar.HOUR_OF_DAY, timeHhSs.split(":")[0].toInt())
                                set(Calendar.MINUTE, timeHhSs.split(":")[1].toInt())
                                set(Calendar.SECOND, ZERO_VALUE)
                            }
                            Log.e(
                                "ChienNgan",
                                "MONTHLY - next remind is ${formatTime(calendar.timeInMillis)}"
                            )
                            alarmManager?.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis,
                                PendingIntent.getBroadcast(
                                    context,
                                    id,
                                    getIntent(
                                        context,
                                        id,
                                        message,
                                        startDate,
                                        timeRemind,
                                        remindOptions
                                    ),
                                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                                ),
                            )
                        }
                    }
                    else -> {
                        return
                    }
                }
            }
        }
    }

    private fun showNotification(context: Context?, message: String) {
        val channelId = "QCPChannelId"
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the notification channel
        val channel = NotificationChannel(
            channelId, "Pomodoro Todo", NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        // Create the notification
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_pomodoro_colored)
            .setContentTitle("Task Reminder").setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true)

        // Show the notification
        notificationManager.notify(
            System.currentTimeMillis().toInt(), notificationBuilder.build()
        )
    }

    private fun timeHhSs(time: Long): String {
        val dateTime = Instant.ofEpochMilli(time)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return formatter.format(dateTime)
    }

    private fun getIntent(
        context: Context?,
        id: Int,
        message: String,
        startDate: Long,
        timeRemind: Long,
        remindOptions: RemindOptions? = null

    ): Intent {
        val intent = Intent(context, AlarmReceiver::class.java)

        val bundle = Bundle().apply {
            putString(AlarmSchedulerImpl.MESSAGE, message)
            putInt(AlarmSchedulerImpl.ID, id)
            putLong(AlarmSchedulerImpl.START_DATE, startDate)
            putLong(AlarmSchedulerImpl.TIME_REMIND, timeRemind)
            remindOptions?.let {
                putParcelable(AlarmSchedulerImpl.REMIND_OPTION, it)
            }
        }

        return intent.apply {
            putExtras(bundle)
        }
    }

    private fun getNextNearestDayMillis(days: List<String>, interval: Int): Calendar? {
        val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        // Step 1: Create a Calendar instance for today's date
        val today = Calendar.getInstance()
        today.timeInMillis = today.timeInMillis + MILLIS_A_DAY * interval

        val dayINWeek = today.get(Calendar.DAY_OF_WEEK)
        // If today is not a Monday, set the date to the Monday of the current week
        if (dayINWeek != Calendar.MONDAY) {
            today.add(Calendar.DAY_OF_WEEK, Calendar.MONDAY - dayINWeek)
        }

        // Step 2: Convert each day in the list to a Calendar instance and set the time to 0:00:00
        val calendars = mutableListOf<Calendar>()
        for (day in days) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, today.get(Calendar.YEAR))
            calendar.set(Calendar.MONTH, today.get(Calendar.MONTH))
            calendar.set(Calendar.DATE, today.get(Calendar.DATE))
            calendar.set(Calendar.HOUR_OF_DAY, ZERO_VALUE)
            calendar.set(Calendar.MINUTE, ZERO_VALUE)
            calendar.set(Calendar.SECOND, ZERO_VALUE)
            calendar.set(Calendar.MILLISECOND, ZERO_VALUE)

            val dayOfWeek = daysOfWeek.indexOf(day)
            val todayDayOfWeek =
                today.get(Calendar.DAY_OF_WEEK) - 1 // Sunday is 1, but we want 0-based index
            val daysToAdd = (dayOfWeek - todayDayOfWeek + 7) % 7
            if (daysToAdd > 0) { // If the day is in the future
                calendar.add(Calendar.DATE, daysToAdd)
            } else if (daysToAdd == 0 && calendar.before(today)) { // If the day is today but in the past, add a week to get the next occurrence
                calendar.add(Calendar.DATE, 7)
            } else { // If the day is today or in the past, add a week to get the next occurrence
                calendar.add(Calendar.DATE, 7 - daysToAdd)
            }
            calendars.add(calendar)
        }

        // Step 3: Sort the list of Calendar instances in ascending order by time
        calendars.sort()

        // Step 4: Iterate over the sorted list and return the time in milliseconds for the first day that is in the future
        for (calendar in calendars) {
            if (calendar.after(today)) {
                return calendar
            }
        }

        // If no future day was found, return null or some default value
        return null
    }

    private fun getTimeInMillisForDayOfMonth(dayOfMonth: Int, monthsToAdd: Long): Long {
        val today = LocalDate.now()
        val todayDate = today.dayOfMonth
        var targetMonth = today.plusMonths(monthsToAdd).month
        val targetYear = today.plusMonths(monthsToAdd).year
        val lastDayOfMonth = targetMonth.length(today.isLeapYear)
        val targetDayOfMonth = when {
            dayOfMonth <= todayDate -> {
                targetMonth = today.plusMonths(monthsToAdd).month
                dayOfMonth
            }
            dayOfMonth in (todayDate + 1)..lastDayOfMonth -> dayOfMonth
            dayOfMonth > lastDayOfMonth -> lastDayOfMonth
            else -> lastDayOfMonth
        }
        val targetDate = LocalDate.of(targetYear, targetMonth, targetDayOfMonth)
        return targetDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    }

    private fun formatTime(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault())
        return dateFormat.format(Date(timeInMillis))
    }
}
