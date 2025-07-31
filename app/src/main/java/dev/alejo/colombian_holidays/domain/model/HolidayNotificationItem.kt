package dev.alejo.colombian_holidays.domain.model

import dev.alejo.colombian_holidays.data.database.entities.HolidayNotificationEntity

data class HolidayNotificationItem(
    val id: Int
)

fun HolidayNotificationItem.toDatabase() = HolidayNotificationEntity(
    id = id
)