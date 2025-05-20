package dev.alejo.colombian_holidays.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alejo.colombian_holidays.core.Response
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import dev.alejo.colombian_holidays.domain.repository.AppPreferencesRepository
import dev.alejo.colombian_holidays.domain.repository.Repository
import dev.alejo.colombian_holidays.domain.usecase.ChangeBackgroundUseCase
import dev.alejo.colombian_holidays.ui.util.ImagesProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        checkBackgroundImage()
        getHolidaysByYear(LocalDate.now().year.toString())
        getNextHoliday()
        getIsTodayHoliday()
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

    fun getHolidaysByYear(year: String) {
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
                    _state.update { currentState ->
                        if(response.data.isNotEmpty() && _state.value.todayHoliday == null) {
                            setTodayHoliday()
                        }
                        currentState.copy(
                            holidays = response.data,
                            isLoadingHolidays = false
                        )
                    }
                }
            }
        }
    }

    private fun getNextHoliday() {
        viewModelScope.launch {
            when (val response = repository.getNextPublicHoliday()) {
                is Response.Error -> {
                    _state.update { currentState ->
                        currentState.copy(
                            error = response.message,
                            isLoadingNextHoliday = false
                        )
                    }
                }

                is Response.Success<PublicHolidayModel> -> {
                    _state.update { currentState ->
                        currentState.copy(
                            nextHoliday = response.data,
                            isLoadingNextHoliday = false
                        )
                    }
                }
            }
        }
    }

    private fun getIsTodayHoliday() {
        viewModelScope.launch {
            when (val response = repository.isTodayHoliday()) {
                is Response.Error -> {
                    _state.update { currentState ->
                        currentState.copy(
                            error = response.message,
                            isLoadingTodayHoliday = false
                        )
                    }
                }

                is Response.Success<Boolean> -> {
                    if(response.data && _state.value.holidays.isNotEmpty()) {
                        setTodayHoliday()
                    }
                    _state.update { currentState ->
                        currentState.copy(
                            isLoadingTodayHoliday = false
                        )
                    }
                }
            }
        }
    }

    private fun setTodayHoliday() {
        val todayHoliday = _state.value.holidays.find { it.date == LocalDate.now().toString() }
        todayHoliday?.let { holiday ->
            _state.update { currentState ->
                currentState.copy(
                    todayHoliday = holiday
                )
            }
        }
    }

    fun setDataLoaded() {
        _state.update { currentState ->
            currentState.copy(
                isDataLoaded = true
            )
        }
    }

}