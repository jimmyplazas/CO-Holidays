package dev.alejo.colombian_holidays.widget

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object WidgetUpdateScheduler {
    fun scheduleDailyUpdate(context: Context) {
        val request = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
            24, TimeUnit.HOURS
        ).setInitialDelay(calculateInitialDelayForMidnight(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "DailyWidgetUpdate",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    private fun calculateInitialDelayForMidnight(): Long {
        val now = LocalDateTime.now()
        val nextUpdate = now.toLocalDate().plusDays(1).atTime(0, 15) // 00:15 AM
        return Duration.between(now, nextUpdate).toMillis()
    }
}
