package dev.alejo.colombian_holidays.ui.core.navigation

sealed class Routes(val route: String) {
    data object Home : Routes("home")
}