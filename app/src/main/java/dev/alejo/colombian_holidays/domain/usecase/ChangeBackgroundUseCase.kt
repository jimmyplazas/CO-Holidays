package dev.alejo.colombian_holidays.domain.usecase

import dev.alejo.colombian_holidays.domain.repository.AppPreferencesRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ChangeBackgroundUseCase(
    private val appPreferencesRepository: AppPreferencesRepository
) {
    operator fun invoke(): Boolean {
        val lastTimeBackgroundChange = appPreferencesRepository.getLastTimeBackgroundChange()
        return lastTimeBackgroundChange?.let { lastDate ->
            val currentDate = LocalDate.now()
            ChronoUnit.DAYS.between(lastDate, currentDate) >= 1
        } ?: true
    }
}