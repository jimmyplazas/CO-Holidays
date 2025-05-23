package dev.alejo.colombian_holidays.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import dev.alejo.colombian_holidays.ui.theme.AppDimens
import dev.alejo.colombian_holidays.ui.util.DateUtils
import java.time.LocalDate
import java.util.Locale

@Composable
fun HolidayItem(holiday: PublicHolidayModel, onClick: (holiday: PublicHolidayModel) -> Unit) {
    val locale = Locale.getDefault()
    val isSpanish = locale.language == "es"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppDimens.Default))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick(holiday) }
            .padding(AppDimens.Default),
        horizontalArrangement = Arrangement.spacedBy(AppDimens.Default),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .width(AppDimens.Small)
                .height(56.dp)
                .clip(RoundedCornerShape(AppDimens.Small))
                .background(MaterialTheme.colorScheme.primary)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val date = holiday.date
            val dayFormatted = DateUtils.formatToDayAbbreviation(date, locale)
            Text(
                text = dayFormatted,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Text(
            text = if(isSpanish) holiday.localName else holiday.name,
            fontSize = 26.sp,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HolidayItemPreview() {
    HolidayItem(
        PublicHolidayModel(
            date = LocalDate.parse("2025-05-05"),
            name = "New Year",
            localName = "Año Nuevo",
            global = true,
            launchYear = 2025,
            types = listOf("Public")
        ),
        onClick = {}
    )
}