package dev.alejo.colombian_holidays.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alejo.colombian_holidays.core.NotificationHelper
import dev.alejo.colombian_holidays.domain.model.HolidayNotificationItem
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import dev.alejo.colombian_holidays.domain.repository.Repository
import dev.alejo.colombian_holidays.ui.util.DateUtils
import dev.alejo.colombian_holidays.ui.util.UiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZoneId

class DetailViewModel(
    private val repository: Repository,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    fun initData(holiday: PublicHolidayModel) {
        val firstWordOfHoliday = holiday.localName.split(" ").first()
        _state.update {
            it.copy(
                holiday = holiday,
                notificationEnabled = DateUtils.isFutureDate(holiday.date),
                notificationId = holiday.date.toEpochDay().toInt() + DateUtils.wordToNumber(
                    firstWordOfHoliday
                )
            )
        }
        notificationHelper.createNotificationChannel()
        getHolidayNotification()
    }

    private fun getHolidayNotification() {
        viewModelScope.launch {
            val notification = repository.getHolidayNotification(_state.value.notificationId)
            _state.update { it.copy(isNotificationChecked = notification != null) }
        }
    }

    fun turnOnNotification() {
        if (DateUtils.isFutureDate(_state.value.holiday!!.date)) {
            viewModelScope.launch {
                val notificationId = _state.value.notificationId
                val holidayNotificationItem = HolidayNotificationItem(id = notificationId)
                val notificationTime = _state.value.holiday!!.date
                    .atTime(8, 0)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
                notificationHelper.scheduleNotification(
                    message = _state.value.holiday!!.localName,
                    notificationId = notificationId,
                    time = notificationTime
                )
                repository.insertHolidayNotification(holidayNotificationItem)
                _state.update { it.copy(isNotificationChecked = true) }
            }
        } else {
            _state.update { it.copy(errorMessage = UiError.PastHolidayError) }
        }
    }

    fun turnOffNotification() {
        viewModelScope.launch {
            val notificationId = _state.value.notificationId
            notificationHelper.removeNotification(notificationId)
            repository.removeHolidayNotification(notificationId)
            _state.update { it.copy(isNotificationChecked = false) }
        }
    }

    fun onNotificationDenied() {
        _state.update {
            it.copy(
                errorMessage = UiError.NotificationDisabledError,
                notificationEnabled = false
            )
        }
    }

    fun onNotificationAllowed() {
        _state.update { it.copy(notificationEnabled = true, errorMessage = null) }
    }

}