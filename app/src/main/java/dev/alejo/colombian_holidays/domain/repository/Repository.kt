package dev.alejo.colombian_holidays.domain.repository

import dev.alejo.colombian_holidays.core.Response
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel

interface Repository {
    suspend fun getHolidaysByYear(year: String): Response<List<PublicHolidayModel>>
}