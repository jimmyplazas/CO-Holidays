package dev.alejo.colombian_holidays.ui.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import dev.alejo.colombian_holidays.ui.theme.AppDimens
import dev.alejo.colombian_holidays.ui.util.DateUtils
import dev.alejo.compose_calendar.CalendarEvent
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ListScreen(
    holidays: List<CalendarEvent<PublicHolidayModel>>,
    currentYearMonth: LocalDate,
    onHolidaySelected: (PublicHolidayModel) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val locale = remember { Locale.getDefault() }
    val groupedHolidays = holidays
        .filter { it.date.year == currentYearMonth.year }
        .groupBy { it.date.monthValue }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(AppDimens.Small),
        contentPadding = PaddingValues(bottom = AppDimens.ListBottomPadding)
    ) {
        groupedHolidays.forEach { (_, holidaysInMonth) ->
            val monthName = DateUtils.formatToMonthName(holidaysInMonth.first().date, locale)

            item {
                Spacer(Modifier.height(AppDimens.Small))
                Text(
                    text = monthName,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            items(holidaysInMonth, key = { it.data!!.name }) { holiday ->
                HolidayItem(
                    holiday = holiday.data!!,
                    animatedVisibilityScope = animatedVisibilityScope,
                ) { onHolidaySelected(it) }
            }
        }
    }
}