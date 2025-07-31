package dev.alejo.colombian_holidays.ui.detail

import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import dev.alejo.colombian_holidays.ui.util.UiError

data class DetailState(
    val holiday: PublicHolidayModel? = null,
    val notificationId: Int = 0,
    val notificationEnabled: Boolean = true,
    val isNotificationChecked: Boolean = false,
    val errorMessage: UiError? = null,
)