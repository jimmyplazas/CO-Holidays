package dev.alejo.colombian_holidays.ui.home

import androidx.annotation.DrawableRes
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import java.time.LocalDate

data class HomeState(
    @DrawableRes val backgroundImage: Int = R.drawable.img_0,
    val holidays: List<PublicHolidayModel> = emptyList(),
    val loadedYears: Set<Int> = emptySet(),
    val nextHoliday: PublicHolidayModel? = null,
    val isLoadingNextHoliday: Boolean = true,
    val isLoadingHolidays: Boolean = true,
    val isLoadingTodayHoliday: Boolean = true,
    val todayHoliday: PublicHolidayModel? = null,
    val lastTodayHolidayResponse: LocalDate? = null,
    val isDataLoaded: Boolean = false,
    val currentMonth: LocalDate = LocalDate.now(),
    val error: String? = null
)