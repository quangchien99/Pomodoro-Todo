package com.chpham.data.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.chpham.domain.alarm.AlarmScheduler
import com.chpham.domain.model.AlarmItem
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AlarmSchedulerImpl(
    private val context: Context,
    private val alarmManager: AlarmManager
) : AlarmScheduler {
    companion object {
        const val MESSAGE = "MESSAGE"
        const val START_DATE = "START_DATE"
        const val TIME_REMIND = "TIME_REMIND"
        const val ID = "ID"
        const val REMIND_OPTION = "REMIND_OPTION"
    }

    @SuppressLint("ShortAlarm")
    override fun schedule(item: AlarmItem) {
        val dateTime = Instant.ofEpochMilli(item.time)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val timeString = formatter.format(dateTime)

        val parts = timeString.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()

        Log.e(
            "ChienNgan",
            "Date= ${item.startDate} == ${formatTime(item.startDate)}, Remind time = ${item.time} == $timeString item= $item"
        )

        val startDate = Calendar.getInstance()
        startDate.timeInMillis = item.startDate
        startDate.apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        Log.e("ChienNgan", "Remind at: ${formatTime(startDate.timeInMillis)}")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            startDate.timeInMillis,
            PendingIntent.getBroadcast(
                context,
                item.id,
                getIntent(item),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )
    }

    private fun getIntent(item: AlarmItem): Intent {
        val intent = Intent(context, AlarmReceiver::class.java)

        val bundle = Bundle().apply {
            putString(MESSAGE, item.message)
            putInt(ID, item.id)
            putLong(START_DATE, item.startDate)
            putLong(TIME_REMIND, item.time)
            item.remindOptions?.let {
                putParcelable(REMIND_OPTION, it)
            }
        }

        return intent.apply {
            putExtras(bundle)
        }
    }

    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )
    }

    private fun formatTime(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault())
        return dateFormat.format(Date(timeInMillis))
    }
}
