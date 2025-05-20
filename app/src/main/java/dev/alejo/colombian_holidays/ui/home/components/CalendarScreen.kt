package dev.alejo.colombian_holidays.ui.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alejo.colombian_holidays.ui.home.HomeState
import dev.alejo.colombian_holidays.ui.theme.AppDimens
import dev.alejo.compose_calendar.CalendarEvent
import dev.alejo.compose_calendar.ComposeCalendar
import dev.alejo.compose_calendar.util.CalendarDefaults
import kotlinx.datetime.LocalDate

@Composable
fun CalendarScreen(
    state: HomeState,
    isSpanish: Boolean,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val events: List<CalendarEvent> = state.holidays.map { holiday ->
        CalendarEvent(
            title = if (isSpanish) holiday.localName else holiday.name,
            description = null,
            date = LocalDate.parse(holiday.date),
            icon = Icons.Default.Star
        )
    }
    ComposeCalendar(
        events = events,
        calendarColors = CalendarDefaults.calendarColors(
            eventBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
            eventContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        onDayClick = { _, _ -> },
        onPreviousMonthClick = { onPreviousMonth() },
        onNextMonthClick = { onNextMonth() }
    )

    val monthHolidays = state.holidays.filter {
        val date = LocalDate.parse(it.date)
        date.month == state.currentMonth.month && date.year == state.currentMonth.year
    }
    AnimatedVisibility(monthHolidays.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(AppDimens.Small),
            contentPadding = PaddingValues(bottom = AppDimens.Default)
        ) {
            items(monthHolidays.size) { index ->
                HolidayItem(monthHolidays[index]) {

                }
            }
        }
    }
}