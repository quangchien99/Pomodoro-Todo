package com.chpham.data.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra(AlarmSchedulerImpl.MESSAGE) ?: return

        val channelId = "QCPChannelId"
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the notification channel
        val channel = NotificationChannel(
            channelId,
            "Pomodoro Todo",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        // Create the notification
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(com.chpham.domain.R.drawable.ic_pomodoro_colored)
            .setContentTitle("Task Reminder")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Show the notification
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
