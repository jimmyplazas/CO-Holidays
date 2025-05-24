package dev.alejo.colombian_holidays.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.ui.home.HolidaysAction
import dev.alejo.colombian_holidays.ui.theme.AppDimens
import dev.alejo.colombian_holidays.ui.theme.AppShapes

@Composable
fun ListBottomNavigation(
    modifier: Modifier,
    year: String,
    onHolidaysAction: (HolidaysAction) -> Unit
) {
    Row(
        modifier = modifier
            .height(AppDimens.ListBottomNavigationHeight)
            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
            .clip(CircleShape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { onHolidaysAction(HolidaysAction.PreviousYear) },
            modifier = Modifier.fillMaxHeight(),
            shape = AppShapes.LeftListNavigationButton,
            contentPadding = PaddingValues(AppDimens.None)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_left),
                contentDescription = null
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = AppDimens.Default),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = year,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }

        Button(
            onClick = { onHolidaysAction(HolidaysAction.NextYear) },
            modifier = Modifier.fillMaxHeight(),
            shape = AppShapes.rightListNavigationButton,
            contentPadding = PaddingValues(AppDimens.None)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = null
            )
        }
    }
}