package dev.alejo.colombian_holidays.domain.model

import java.time.LocalDate

data class PublicHolidayModel(
    val date: LocalDate,
    val localName: String,
    val name: String,
    val global: Boolean,
    val launchYear: Int?,
    val types: List<String>
)