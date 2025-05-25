package dev.alejo.colombian_holidays.domain.repository

import java.time.LocalDate

interface AppPreferencesRepository {
    fun getLastTimeBackgroundChange(): LocalDate?
    fun saveBackgroundChangeDate(date: LocalDate)
}