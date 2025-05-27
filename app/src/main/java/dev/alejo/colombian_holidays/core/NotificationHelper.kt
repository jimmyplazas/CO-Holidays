package dev.alejo.colombian_holidays.core

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import androidx.core.net.toUri
import dev.alejo.colombian_holidays.R

class NotificationHelper(private val context: Context) {

    private val name = "Notification Channel"
    private val description = "Channel for holiday reminders"
    private val channelId = "colombian_holidays_notifications_channel"
    private val messageExtra = "messageExtra"
    private val notificationIdExtra = "notificationIdExtra"

    fun createNotificationChannel() {
        val name = name
        val description = description
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        val channel = NotificationChannel(channelId, name, importance).apply {
            this.description = description
            setSound(
                "android.resource://${context.packageName}/${R.raw.notification_sound}".toUri(),
                audioAttributes
            )
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(message: String, notificationId: Int, time: Long) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(messageExtra, message)
            putExtra(notificationIdExtra, notificationId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    fun removeNotification(notificationId: Int) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(messageExtra, "")
            putExtra(notificationIdExtra, notificationId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
