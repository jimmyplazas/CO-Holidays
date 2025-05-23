package dev.alejo.colombian_holidays.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alejo.colombian_holidays.core.Response
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import dev.alejo.colombian_holidays.domain.repository.AppPreferencesRepository
import dev.alejo.colombian_holidays.domain.repository.Repository
import dev.alejo.colombian_holidays.domain.usecase.ChangeBackgroundUseCase
import dev.alejo.colombian_holidays.ui.util.ImagesProvider
import dev.alejo.compose_calendar.CalendarEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(
    private val changeBackgroundUseCase: ChangeBackgroundUseCase,
    private val appPreferencesRepository: AppPreferencesRepository,
    private val repository: Repository,
    private val imagesProvider: ImagesProvider
) : ViewModel() {

    sealed class HolidayLoadTarget {
        data object Initial : HolidayLoadTarget()
        data object PreviousYear : HolidayLoadTarget()
        data object NextYear : HolidayLoadTarget()
    }

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    val events: StateFlow<List<CalendarEvent<PublicHolidayModel>>> = _state
        .map { state ->
            state.holidays.map { holiday ->
                CalendarEvent(
                    data = holiday,
                    date = holiday.date
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    init {
        checkBackgroundImage()
        getHolidaysByYear(LocalDate.now().year.toString())
    }

    private fun checkBackgroundImage() {
        if (changeBackgroundUseCase()) {
            val newImage = imagesProvider.getImages().random()
            appPreferencesRepository.saveBackgroundChangeDate(LocalDate.now())
            _state.update { currentState ->
                currentState.copy(
                    backgroundImage = newImage
                )
            }
        }
    }

    private fun getHolidaysByYear(
        year: String,
        loadTarget: HolidayLoadTarget = HolidayLoadTarget.Initial
    ) {
        viewModelScope.launch {
            when (val response = repository.getHolidaysByYear(year)) {
                is Response.Error -> {
                    _state.update { currentState ->
                        currentState.copy(
                            error = response.message,
                            isLoadingHolidays = false
                        )
                    }
                }

                is Response.Success<List<PublicHolidayModel>> -> {
                    val data = response.data

                    if (data.isNotEmpty() && loadTarget is HolidayLoadTarget.Initial) {
                        setTodayHoliday(response.data)
                    }

                    if (_state.value.nextHoliday == null) {
                        setNextHoliday(response.data)
                    }

                    val newYears = data.map { it.date.year }.toSet()
                    val holidaysData = when(loadTarget) {
                        is HolidayLoadTarget.Initial -> data
                        is HolidayLoadTarget.PreviousYear -> data + _state.value.holidays
                        is HolidayLoadTarget.NextYear -> _state.value.holidays + data
                    }
                    _state.update { currentState ->
                        currentState.copy(
                            holidays = holidaysData,
                            loadedYears = currentState.loadedYears + newYears,
                            isLoadingHolidays = false
                        )
                    }
                }
            }
        }
    }

    private fun setTodayHoliday(holidays: List<PublicHolidayModel>) {
        val todayHoliday = holidays.find { it.date == LocalDate.now() }
        todayHoliday?.let { holiday ->
            _state.update { currentState ->
                currentState.copy(
                    todayHoliday = holiday
                )
            }
        }
    }

    private fun setNextHoliday(holidays: List<PublicHolidayModel>) {
        val today = LocalDate.now()
        val nextHoliday = holidays.firstOrNull { it.date.isAfter(today) }
        _state.update { currentState ->
            currentState.copy(
                nextHoliday = nextHoliday
            )
        }
    }

    fun previousMonth() {
        val currentMonthUpdated = _state.value.currentMonth.minusMonths(1)
        _state.update { currentState -> currentState.copy(currentMonth = currentMonthUpdated) }
        loadMoreHolidays(currentMonthDate = currentMonthUpdated, isGoingToPreviousMonths = true)
    }

    fun nextMonth() {
        val currentMonthUpdated = _state.value.currentMonth.plusMonths(1)
        _state.update { currentState -> currentState.copy(currentMonth = currentMonthUpdated) }
        loadMoreHolidays(currentMonthDate = currentMonthUpdated, isGoingToPreviousMonths = false)
    }

    private fun loadMoreHolidays(
        currentMonthDate: LocalDate,
        isGoingToPreviousMonths: Boolean = false
    ) {
        /*
        Load previous year holidays if user navigates to Jan, Feb, or Mar
        Load next year holidays if user navigates to Oct, Nov, or Dec
         */
        val shouldLoadYear = if (isGoingToPreviousMonths) {
            currentMonthDate.month.value in 1..3
        } else {
            currentMonthDate.month.value in 10..12
        }

        if (shouldLoadYear) {
            val year = if (isGoingToPreviousMonths) currentMonthDate.year - 1 else currentMonthDate.year + 1
            if (year !in _state.value.loadedYears) {
                getHolidaysByYear(
                    year = year.toString(),
                    loadTarget = if (isGoingToPreviousMonths) {
                        HolidayLoadTarget.PreviousYear
                    } else {
                        HolidayLoadTarget.NextYear
                    }
                )
            }
        }
    }

}