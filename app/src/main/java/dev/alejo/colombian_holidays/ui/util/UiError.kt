package dev.alejo.colombian_holidays.ui.util

sealed class UiError{
    data object PastHolidayError : UiError()
    data object NotificationDisabledError : UiError()
}