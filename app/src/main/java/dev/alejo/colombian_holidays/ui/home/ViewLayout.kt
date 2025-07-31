package dev.alejo.colombian_holidays.ui.home

sealed class ViewLayout {
    data object Calendar : ViewLayout()
    data object List : ViewLayout()
}