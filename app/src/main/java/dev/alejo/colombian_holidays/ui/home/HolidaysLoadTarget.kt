package dev.alejo.colombian_holidays.ui.home

sealed class HolidaysLoadTarget {
    data object Initial : HolidaysLoadTarget()
    data object PreviousYear : HolidaysLoadTarget()
    data object NextYear : HolidaysLoadTarget()
}