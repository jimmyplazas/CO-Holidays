package dev.alejo.colombian_holidays.ui.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.ui.home.ViewLayout
import dev.alejo.colombian_holidays.ui.theme.AppDimens

@Composable
fun BottomSheetTop(modifier: Modifier, viewLayout: ViewLayout, onViewLayoutClick: () -> Unit) {
    Box(modifier = modifier) {
        Text(
            text = stringResource(R.string.all_holidays),
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(AppDimens.Small))
                .background(MaterialTheme.colorScheme.primary),
            onClick = { onViewLayoutClick() }
        ) {
            AnimatedContent(targetState = viewLayout) { layout ->
                when (layout) {
                    ViewLayout.Calendar -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    ViewLayout.List -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}