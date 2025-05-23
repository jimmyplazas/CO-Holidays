package dev.alejo.colombian_holidays.ui.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import dev.alejo.colombian_holidays.ui.theme.AppDimens
import dev.alejo.compose_calendar.CalendarEvent
import dev.alejo.compose_calendar.ComposeCalendar
import dev.alejo.compose_calendar.util.CalendarDefaults
import java.time.LocalDate

@Composable
fun CalendarScreen(
    holidays: List<CalendarEvent<PublicHolidayModel>>,
    currentMonth: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    ComposeCalendar(
        events = holidays,
        calendarColors = CalendarDefaults.calendarColors(
            eventBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
            eventContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        onDayClick = { _, _ -> },
        onPreviousMonthClick = { onPreviousMonth() },
        onNextMonthClick = { onNextMonth() },
        eventIndicator = {_, _, _ ->
            HolidayIndicator()
        },
        maxIndicators = CalendarDefaults.IndicatorLimit.Two
    )

    val monthHolidays = holidays
        .filter { it.data!!.date.month == currentMonth.month && it.data!!.date.year == currentMonth.year }

    AnimatedContent(monthHolidays.isEmpty()) { noHolidays ->
        if (noHolidays) {
            Image(
                painter = painterResource(id = R.drawable.no_holidays_img),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.ExtraLarge),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppDimens.Small),
                contentPadding = PaddingValues(bottom = AppDimens.Default)
            ) {
                items(monthHolidays.size) { index ->
                    HolidayItem(monthHolidays[index].data!!) {

                    }
                }
            }
        }
    }
}

@Composable
fun HolidayIndicator() {
    Box(
        modifier = Modifier
            .size(AppDimens.Small)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    )
}