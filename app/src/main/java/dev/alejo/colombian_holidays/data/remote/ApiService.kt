package dev.alejo.colombian_holidays.data.remote

import dev.alejo.colombian_holidays.data.remote.response.PublicHolidayResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val COLOMBIA_CODE = "CO"

class ApiService(private val client: HttpClient) {
    suspend fun getHolidaysByYear(year: String): List<PublicHolidayResponse> = client
        .get("PublicHolidays/${year}/$COLOMBIA_CODE").body()
}