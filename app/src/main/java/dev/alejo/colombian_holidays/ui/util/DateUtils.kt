package dev.alejo.colombian_holidays.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
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

    fun formatToMonthName(date: LocalDate, locale: Locale): String {
        return date.month.getDisplayName(TextStyle.FULL, locale)
            .replaceFirstChar { it.uppercase(locale) }
    }

    fun formatDateLocalized(date: LocalDate, locale: Locale = Locale.getDefault()): String {
        val pattern = when (locale.language) {
            "es" -> "d 'de' MMMM 'de' yyyy"
            else -> "MMMM d, yyyy"
        }

        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        return date.format(formatter)
    }

    fun wordToNumber(charSequence: String): Int {
        var sum = 0
        charSequence.forEach { sum += it.uppercaseChar() - 'A' + 1 }
        return sum
    }

    fun isFutureDate(date: LocalDate): Boolean {
        val today = LocalDate.now()
        return date.isAfter(today)
    }
}