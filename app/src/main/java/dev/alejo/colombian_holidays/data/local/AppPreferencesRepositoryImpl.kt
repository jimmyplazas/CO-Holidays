package dev.alejo.colombian_holidays.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import dev.alejo.colombian_holidays.domain.repository.AppPreferencesRepository
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class AppPreferencesRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : AppPreferencesRepository {
    override fun getLastTimeBackgroundChange(): LocalDate? {
        val millis = sharedPreferences.getLong(AppPreferencesKeys.LAST_TIME_BACKGROUND_CHANGE, -1L)
        return if (millis != -1L) {
            Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
        } else {
            null
        }
    }

    override fun saveBackgroundChangeDate(date: LocalDate) {
        sharedPreferences.edit {
            putLong(
                AppPreferencesKeys.LAST_TIME_BACKGROUND_CHANGE,
                date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
        }
    }

    override fun getWidgetTipShown(): Boolean = sharedPreferences
        .getBoolean(AppPreferencesKeys.WIDGET_TIP_SHOWN, false)

    override fun saveWidgetTipShown() {
        sharedPreferences.edit {
            putBoolean(AppPreferencesKeys.WIDGET_TIP_SHOWN, true)
        }
    }
}