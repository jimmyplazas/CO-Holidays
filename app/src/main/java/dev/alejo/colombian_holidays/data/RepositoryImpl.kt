package dev.alejo.colombian_holidays.data

import dev.alejo.colombian_holidays.core.Response
import dev.alejo.colombian_holidays.data.database.dao.HolidayNotificationDao
import dev.alejo.colombian_holidays.data.database.entities.toDomain
import dev.alejo.colombian_holidays.data.remote.ApiService
import dev.alejo.colombian_holidays.data.remote.response.toDomain
import dev.alejo.colombian_holidays.domain.model.HolidayNotificationItem
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import dev.alejo.colombian_holidays.domain.model.toDatabase
import dev.alejo.colombian_holidays.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImpl(
    private val api: ApiService,
    private val dao: HolidayNotificationDao
) : Repository {

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

    override suspend fun getHolidayNotification(holidayNotificationId: Int): HolidayNotificationItem? =
        dao.getHolidayNotification(holidayNotificationId)?.toDomain()

    override suspend fun insertHolidayNotification(holidayNotification: HolidayNotificationItem) {
        dao.insertHolidayNotification(holidayNotification.toDatabase())
    }

    override suspend fun removeHolidayNotification(holidayNotificationId: Int) {
        dao.removeHolidayNotification(holidayNotificationId)
    }

}