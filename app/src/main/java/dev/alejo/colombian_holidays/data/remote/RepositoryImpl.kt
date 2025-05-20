package dev.alejo.colombian_holidays.data.remote

import dev.alejo.colombian_holidays.core.Response
import dev.alejo.colombian_holidays.data.remote.response.toDomain
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import dev.alejo.colombian_holidays.domain.repository.Repository
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImpl(private val api: ApiService) : Repository {
    override suspend fun isTodayHoliday(): Response<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = api.isTodayHoliday()
            when (response.status) {
                HttpStatusCode.OK -> {
                    Response.Success(data = true)
                }

                HttpStatusCode.NoContent -> {
                    Response.Success(data = false)
                }

                else -> {
                    Response.Error("Error inesperado")
                }
            }
        } catch (e: Exception) {
            Response.Error(e.message ?: "Ha ocurrido un error")
        }
    }

    override suspend fun getHolidaysByYear(
        year: String
    ): Response<List<PublicHolidayModel>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getHolidaysByYear(year)
            Response.Success(data = response.map { holiday -> holiday.toDomain() })
        } catch (e: Exception) {
            Response.Error(e.message ?: "Ha ocurrido un error")
        }
    }

    override suspend fun getNextPublicHoliday(): Response<PublicHolidayModel> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getNextPublicHoliday()
                Response.Success(data = response.first().toDomain())
            } catch (e: Exception) {
                Response.Error(e.message ?: "Ha ocurrido un error")
            }
        }
}