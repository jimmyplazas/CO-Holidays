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
    val todayHoliday: PublicHolidayModel? = null,
    val isLoadingHolidays: Boolean = true,
    val currentYearMonth: LocalDate = LocalDate.now(),
    val viewLayout: ViewLayout = ViewLayout.List,
    val error: String? = null
)