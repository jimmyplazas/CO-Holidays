package dev.alejo.colombian_holidays.ui.home

sealed class HolidaysAction {
    data object PreviousMonth : HolidaysAction()
    data object NextMonth : HolidaysAction()
    data object PreviousYear : HolidaysAction()
    data object NextYear : HolidaysAction()
}

fun HolidaysAction.toLoadTarget(): HolidaysLoadTarget = when(this) {
    HolidaysAction.NextMonth, HolidaysAction.NextYear -> HolidaysLoadTarget.NextYear
    HolidaysAction.PreviousMonth, HolidaysAction.PreviousYear -> HolidaysLoadTarget.PreviousYear
}