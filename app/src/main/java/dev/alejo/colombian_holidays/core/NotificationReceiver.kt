package dev.alejo.colombian_holidays.core

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import dev.alejo.colombian_holidays.MainActivity
import dev.alejo.colombian_holidays.R

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val message = intent.getStringExtra(MESSAGE_EXTRA)
        val notificationId = intent.getIntExtra(NOTIFICATION_ID_EXTRA, 0)

        val homeIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(NOTIFICATION_ID_EXTRA, notificationId)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            homeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_calendar)
            .setContentTitle(context.getString(R.string.today_is_holiday))
            .setContentText(message)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(
                "android.resource://${context.packageName}/${R.raw.notification_sound}".toUri()
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }

    companion object {
        const val CHANNEL_ID = "colombian_holidays_notifications_channel"
        const val MESSAGE_EXTRA = "messageExtra"
        const val NOTIFICATION_ID_EXTRA = "notificationIdExtra"
    }
}
