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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val MILLIS_A_DAY = 86400000L
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        if (context == null) {
            return
        }

        val message = intent?.getStringExtra(AlarmSchedulerImpl.MESSAGE) ?: return

        val id = intent.getIntExtra(AlarmSchedulerImpl.ID, -1)
        if (id == -1) {
            return
        }

        val startDate = intent.getLongExtra(AlarmSchedulerImpl.START_DATE, -1L)
        if (startDate == -1L) {
            return
        }

        val timeRemind = intent.getLongExtra(AlarmSchedulerImpl.TIME_REMIND, -1L)
        if (timeRemind == -1L) {
            return
        }

        val remindOptions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(AlarmSchedulerImpl.REMIND_OPTION, RemindOptions::class.java)
        } else {
            intent.getParcelableExtra(AlarmSchedulerImpl.REMIND_OPTION)
        }

        showNotification(context, message)

        val todayDate = Calendar.getInstance().apply {
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        Log.e("ChienNgan", "today Date= ${todayDate.timeInMillis}")
        Log.e("ChienNgan", "onReceive: startDate= $startDate, remind = $remindOptions")
        remindOptions?.let { remind ->
            if (remind.mode == RemindOptions.RemindMode.UN_SPECIFIED) {
                return
            } else {
                // Still in interval
                val nextRemindTime = todayDate.timeInMillis + (remind.interval * MILLIS_A_DAY)
                if (nextRemindTime - startDate <= remind.endInt.toLong()) {

                    val alarmManager =
                        context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

                    when (remind.mode) {
                        RemindOptions.RemindMode.DAILY -> {

                            val timeHhSs = timeHhSs(timeRemind)
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = nextRemindTime
                            calendar.apply {
                                set(Calendar.HOUR_OF_DAY, timeHhSs.split(":")[0].toInt())
                                set(Calendar.MINUTE, timeHhSs.split(":")[1].toInt())
                                set(Calendar.SECOND, 0)
                            }

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
                                val timeHhSs = timeHhSs(timeRemind)
                                val calendar = Calendar.getInstance()
                                calendar.timeInMillis = nextRemindTime
                                calendar.apply {
                                    set(Calendar.HOUR_OF_DAY, timeHhSs.split(":")[0].toInt())
                                    set(Calendar.MINUTE, timeHhSs.split(":")[1].toInt())
                                    set(Calendar.SECOND, 0)
                                }

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
                            }
                        }
                        RemindOptions.RemindMode.MONTHLY -> {
                        }
                        else -> {
                            return
                        }
                    }
                } else {
                    return
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

    fun timeHhSs(time: Long): String {
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
}
