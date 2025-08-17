package dev.alejo.colombian_holidays.boot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.alejo.colombian_holidays.widget.WidgetUpdateScheduler

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        if (intent.action != "android.intent.action.BOOT_COMPLETED") return
        WidgetUpdateScheduler.start(context)
    }
}