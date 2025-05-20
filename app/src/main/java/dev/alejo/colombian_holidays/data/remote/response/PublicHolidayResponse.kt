package dev.alejo.colombian_holidays.data.remote.response

import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PublicHolidayResponse(
    @SerialName("date") val date: String,
    @SerialName("localName") val localName: String,
    @SerialName("name") val name: String,
    @SerialName("global") val global: Boolean,
    @SerialName("launchYear") val launchYear: Int?,
    @SerialName("types") val types: List<String>
)

fun PublicHolidayResponse.toDomain(): PublicHolidayModel = PublicHolidayModel(
    date = date,
    localName = localName,
    name = name,
    global = global,
    launchYear = launchYear,
    types = types
)