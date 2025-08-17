package dev.alejo.colombian_holidays.widget

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object WidgetUpdateScheduler {

    fun start(context: Context) {
        val request = OneTimeWorkRequestBuilder<WidgetUpdateWorker>()
            .setInitialDelay(0, TimeUnit.MILLISECONDS)
            .build()

        setWorkManager(context, request)
    }

    fun scheduleNextUpdate(context: Context) {
        val delay = calculateInitialDelayForMidnight()
        val request = OneTimeWorkRequestBuilder<WidgetUpdateWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()
        setWorkManager(context, request)
    }

    private fun setWorkManager(context: Context, request: OneTimeWorkRequest) {
        WorkManager.getInstance(context).enqueueUniqueWork(
            "DailyWidgetUpdate",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    private fun calculateInitialDelayForMidnight(): Long {
        val now = LocalDateTime.now()
        var nextUpdate = now.toLocalDate().atTime(0, 0)
        if (!nextUpdate.isAfter(now)) {
            nextUpdate = nextUpdate.plusDays(1)
        }
        return Duration.between(now, nextUpdate).toMillis()
    }

}
