package dev.alejo.colombian_holidays.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    @Composable
    fun getNextHolidayMessage(nextHoliday: PublicHolidayModel): String {
        val locale = Locale.getDefault()
        val date = nextHoliday.date

        return when (locale.language) {
            "es" -> {
                val formattedDate = date.format(
                    DateTimeFormatter.ofPattern("EEEE, d 'de' MMM", Locale("es"))
                )

                "${stringResource(R.string.upcoming_holiday)} $formattedDate ${stringResource(R.string.for_description)} ${nextHoliday.localName}"
            }

            else -> {
                val formattedDate = date.format(
                    DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.ENGLISH)
                )

                "${stringResource(R.string.upcoming_holiday)} $formattedDate ${stringResource(R.string.for_description)} ${nextHoliday.name}"
            }
        }
    }

    fun formatToDayAbbreviation(date: LocalDate, locale: Locale): String {
        val formatter = DateTimeFormatter.ofPattern("EEE", locale)
        return formatter.format(date).replaceFirstChar { it.uppercase(locale) }
    }
}