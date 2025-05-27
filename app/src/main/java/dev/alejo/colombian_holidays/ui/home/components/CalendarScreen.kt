package dev.alejo.colombian_holidays.ui.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import dev.alejo.colombian_holidays.ui.home.HolidaysAction
import dev.alejo.colombian_holidays.ui.theme.AppDimens
import dev.alejo.compose_calendar.CalendarEvent
import dev.alejo.compose_calendar.ComposeCalendar
import dev.alejo.compose_calendar.util.CalendarDefaults
import java.time.LocalDate

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CalendarScreen(
    holidays: List<CalendarEvent<PublicHolidayModel>>,
    currentMonth: LocalDate,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onHolidaysAction: (HolidaysAction) -> Unit,
    onHolidaySelected: (PublicHolidayModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppDimens.Default)
            .padding(top = AppDimens.Default),
        verticalArrangement = Arrangement.spacedBy(AppDimens.Default),
    ) {
        val monthHolidays = holidays
            .filter { it.date.month == currentMonth.month && it.date.year == currentMonth.year }

        ComposeCalendar(
            events = holidays,
            initDate = currentMonth,
            calendarColors = CalendarDefaults.calendarColors(
                eventBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                eventContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onDayClick = { _, _ -> },
            onPreviousMonthClick = { onHolidaysAction(HolidaysAction.PreviousMonth) },
            onNextMonthClick = { onHolidaysAction(HolidaysAction.NextMonth) },
            eventIndicator = { _, _, _ ->
                HolidayIndicator()
            },
            isContentClickable = false,
            maxIndicators = CalendarDefaults.IndicatorLimit.Two
        )

        AnimatedContent(targetState = monthHolidays.isEmpty()) { noHolidays ->
            if (noHolidays) {
                Box(Modifier.fillMaxWidth().padding(horizontal = AppDimens.NoHolidaysHorizontalPadding)) {
                    Image(
                        painter = painterResource(id = R.drawable.no_holidays_img),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(AppDimens.Small),
                    contentPadding = PaddingValues(bottom = AppDimens.Default)
                ) {
                    items(monthHolidays.size) { index ->
                        HolidayItem(
                            holiday = monthHolidays[index].data!!,
                            animatedVisibilityScope = animatedVisibilityScope
                        ) { onHolidaySelected(it) }
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

@Preview(showBackground = true)
@Composable
private fun CalendarScreenPrev() {
//    CalendarScreen(
//        holidays = emptyList(),
//        currentMonth = LocalDate.now(),
//        onHolidaysAction = {},
//        onHolidaySelected = {}
//    )
}