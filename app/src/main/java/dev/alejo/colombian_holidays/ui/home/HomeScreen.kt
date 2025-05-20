package dev.alejo.colombian_holidays.ui.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import dev.alejo.colombian_holidays.ui.home.components.CalendarScreen
import dev.alejo.colombian_holidays.ui.theme.AppDimens
import dev.alejo.colombian_holidays.ui.util.DateUtils
import java.util.Locale

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(state: HomeState, onPreviousMonth: () -> Unit, onNextMonth: () -> Unit) {
    val scaffoldState = rememberBottomSheetScaffoldState()
//    val scaffoldState = rememberBottomSheetScaffoldState(
//        rememberStandardBottomSheetState(
//            initialValue = SheetValue.Expanded
//        )
//    )
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val maxSheetHeight = screenHeight - AppDimens.PeakSheetTopMargin
    val locale = remember { Locale.getDefault() }
    val isSpanish = locale.language == "es"

    Box(Modifier.fillMaxSize()) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                BottomSheetContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = maxSheetHeight),
                    state = state,
                    isSpanish = isSpanish,
                    onPreviousMonth = { onPreviousMonth() },
                    onNextMonth = { onNextMonth() }
                )
            },
            sheetPeekHeight = AppDimens.DefaultPeekHeight
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to MaterialTheme.colorScheme.secondary,
                            0.3f to Color.Black.copy(alpha = 0.8f),
                            0.4f to Color.Black,
                            1f to Color.Black
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = state.backgroundImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.alpha(0.35f)
                )
                AnimatedContent(state.isDataLoaded) { isDataLoaded ->
                    if (isDataLoaded) {
                        MainContent(state, isSpanish)
                    } else {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent(state: HomeState, isSpanish: Boolean) {
    val mainText = when {
        state.todayHoliday == null -> stringResource(R.string.today_is_not_holiday)
        isSpanish -> state.todayHoliday.localName
        else -> state.todayHoliday.name
    }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = AppDimens.Default),
        verticalArrangement = Arrangement.spacedBy(AppDimens.Default)
    ) {
        if (state.todayHoliday != null) {
            Text(
                text = stringResource(R.string.today_is_holiday),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.White
            )
        }
        Text(
            text = mainText,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        AnimatedVisibility(state.nextHoliday != null) {
            state.nextHoliday?.let { nextHoliday ->
                val nextHolidayMessage = DateUtils.getNextHolidayMessage(nextHoliday)
                Text(
                    text = nextHolidayMessage,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun BottomSheetContent(
    modifier: Modifier,
    state: HomeState,
    isSpanish: Boolean,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppDimens.Default),
            verticalArrangement = Arrangement.spacedBy(AppDimens.Default)
        ) {
            BottomSheetTop()
            CalendarScreen(state, isSpanish, onPreviousMonth, onNextMonth)
        }
    }
}

@Composable
fun BottomSheetTop() {
    Box(Modifier.fillMaxWidth()) {
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
                .clip(RoundedCornerShape(AppDimens.Small)),
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val holidays = listOf(
        PublicHolidayModel(
            date = "2025-05-05",
            name = "New Year",
            localName = "Año Nuevo",
            global = true,
            launchYear = 2025,
            types = listOf("Public")
        ),
        PublicHolidayModel(
            date = "2025-04-01",
            name = "New Year",
            localName = "Año Nuevo",
            global = true,
            launchYear = 2025,
            types = listOf("Public")
        ),
        PublicHolidayModel(
            date = "2025-07-25",
            name = "New Year",
            localName = "Año Nuevo",
            global = true,
            launchYear = 2025,
            types = listOf("Public")
        )
    )
    HomeScreen(
        state = HomeState(
            holidays = holidays,
            nextHoliday = PublicHolidayModel(
                date = "2025-01-01",
                name = "New Year",
                localName = "Año Nuevo",
                global = true,
                launchYear = 2025,
                types = listOf("Public")
            ),
            isLoadingNextHoliday = false,
            isLoadingHolidays = false,
            isLoadingTodayHoliday = false,
            isDataLoaded = true,
            todayHoliday = PublicHolidayModel(
                date = "2025-05-20",
                name = "New Year",
                localName = "Año Nuevo",
                global = true,
                launchYear = 2025,
                types = listOf("Public")
            )
        ),
        onPreviousMonth = {},
        onNextMonth = {}
    )
}