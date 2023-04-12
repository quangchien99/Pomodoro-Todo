package com.chpham.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.chpham.domain.alarm.AlarmScheduler
import com.chpham.domain.model.AlarmItem
import com.chpham.domain.model.RemindOptions

class AlarmSchedulerImpl(
    private val context: Context,
    private val alarmManager: AlarmManager
) : AlarmScheduler {
    companion object {
        const val MESSAGE = "MESSAGE"
    }

    override fun schedule(item: AlarmItem) {
        if (item.remindOptions == null || item.remindOptions?.mode == RemindOptions.RemindMode.UN_SPECIFIED) {
            val triggerAfter = item.time - System.currentTimeMillis()
            Log.e("ChienNgan", "Trigger After: $triggerAfter")
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAfter,
                PendingIntent.getBroadcast(
                    context,
                    item.hashCode(),
                    getIntent(item),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                ),
            )
        } else {
            // TODO: implement repetition
        }
    }

    private fun getIntent(item: AlarmItem): Intent {
        val intent = Intent(context, AlarmReceiver::class.java)
        val bundle = Bundle().apply {
            putString(MESSAGE, item.message)
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
}
