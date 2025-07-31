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
        loadTarget: HolidaysLoadTarget = HolidaysLoadTarget.Initial
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

                    if (data.isNotEmpty() && loadTarget is HolidaysLoadTarget.Initial) {
                        setTodayHoliday(response.data)
                    }

                    if (_state.value.nextHoliday == null) {
                        setNextHoliday(response.data)
                    }

                    val newYears = data.map { it.date.year }.toSet()
                    val holidaysData = when (loadTarget) {
                        is HolidaysLoadTarget.Initial -> data
                        is HolidaysLoadTarget.PreviousYear -> data + _state.value.holidays
                        is HolidaysLoadTarget.NextYear -> _state.value.holidays + data
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

    fun onHolidaysAction(holidaysAction: HolidaysAction) {
        val currentDate = _state.value.currentYearMonth.adjustWith(holidaysAction)
        _state.update { it.copy(currentYearMonth = currentDate) }

        if (shouldLoadYear(currentDate, holidaysAction)) {
            val year = getTargetYear(currentDate, holidaysAction)
            if (year !in _state.value.loadedYears) {
                _state.update { it.copy(isLoadingHolidays = true) }
                getHolidaysByYear(
                    year = year.toString(),
                    loadTarget = holidaysAction.toLoadTarget()
                )
            }
        }
    }

    private fun LocalDate.adjustWith(action: HolidaysAction): LocalDate = when (action) {
        HolidaysAction.NextMonth -> plusMonths(1)
        HolidaysAction.PreviousMonth -> minusMonths(1)
        HolidaysAction.NextYear -> plusYears(1)
        HolidaysAction.PreviousYear -> minusYears(1)
    }

    private fun shouldLoadYear(date: LocalDate, action: HolidaysAction): Boolean = when (action) {
        HolidaysAction.PreviousMonth -> date.monthValue in 1..3
        HolidaysAction.NextMonth -> date.monthValue in 10..12
        HolidaysAction.PreviousYear, HolidaysAction.NextYear -> true
    }

    private fun getTargetYear(date: LocalDate, action: HolidaysAction): Int = when (action) {
        HolidaysAction.PreviousYear, HolidaysAction.NextYear -> date.year
        HolidaysAction.PreviousMonth -> date.year - 1
        HolidaysAction.NextMonth -> date.year + 1
    }

    fun onViewLayoutClick() {
        _state.update {
            it.copy(
                viewLayout = when (it.viewLayout) {
                    ViewLayout.Calendar -> ViewLayout.List
                    ViewLayout.List -> ViewLayout.Calendar
                }
            )
        }
    }

    fun widgetTipShown(): Boolean = appPreferencesRepository.getWidgetTipShown()

    fun setWidgetTipShown() = appPreferencesRepository.saveWidgetTipShown()
}