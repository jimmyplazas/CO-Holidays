package dev.alejo.colombian_holidays.domain.model

data class PublicHolidayModel(
    val date: String,
    val localName: String,
    val name: String,
    val global: Boolean,
    val launchYear: Int?,
    val types: List<String>
)