package dev.alejo.colombian_holidays.ui.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    data object Home : Routes()

    @Serializable
    data class Detail(
        val date: String,
        val localName: String,
        val name: String,
        val global: Boolean,
        val launchYear: Int?,
        val types: List<String>
    ) : Routes()

}