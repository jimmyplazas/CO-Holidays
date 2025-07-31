@file:OptIn(ExperimentalSharedTransitionApi::class)

package dev.alejo.colombian_holidays.ui.detail

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import dev.alejo.colombian_holidays.ui.theme.AppDimens
import dev.alejo.colombian_holidays.ui.util.DateUtils
import dev.alejo.colombian_holidays.ui.util.UiError
import java.time.LocalDate
import java.util.Locale

@Composable
fun SharedTransitionScope.DetailScreen(
    state: DetailState,
    onTurnOnNotification: () -> Unit,
    onTurnOffNotification: () -> Unit,
    onBack: () -> Unit,
    onNotificationDenied: () -> Unit,
    onNotificationAllowed: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val locale = Locale.getDefault()
    val isSpanish = locale.language == "es"
    val holidayName =
        if (isSpanish) state.holiday?.localName.orEmpty() else state.holiday?.name.orEmpty()

    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            checkExactAlarmPermission(context, onNotificationDenied = onNotificationDenied) {
                onNotificationAllowed()
            }
        } else {
            onNotificationDenied()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = AppDimens.ExtraLarge),
        verticalArrangement = Arrangement.spacedBy(AppDimens.Default),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackButton(
            Modifier
                .align(Alignment.Start)
                .padding(start = AppDimens.Default)
        ) { onBack() }
        DetailTop(state.holiday?.date, holidayName, animatedVisibilityScope)
        DetailContent(
            holiday = state.holiday,
            checked = state.isNotificationChecked,
            onNotificationCheck = { isChecked ->
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val permissionCheck = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.POST_NOTIFICATIONS
                        )
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            checkExactAlarmPermission(context, onNotificationDenied = onNotificationDenied) {
                                onTurnOnNotification()
                            }
                        } else {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    } else {
                        checkExactAlarmPermission(context, onNotificationDenied = onNotificationDenied) {
                            onTurnOnNotification()
                        }
                    }
                } else {
                    onTurnOffNotification()
                }
            },
            notificationEnabled = state.notificationEnabled,
            error = state.errorMessage
        )
    }
}

@Composable
fun BackButton(modifier: Modifier, onBack: () -> Unit) {
    IconButton(
        modifier = modifier,
        onClick = onBack
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_left),
            contentDescription = null,
            modifier = Modifier.size(AppDimens.Large),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun DetailContent(
    holiday: PublicHolidayModel?,
    checked: Boolean,
    notificationEnabled: Boolean,
    onNotificationCheck: (Boolean) -> Unit,
    error: UiError?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(
                RoundedCornerShape(
                    topStart = AppDimens.ExtraLarge,
                    topEnd = AppDimens.ExtraLarge
                )
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(AppDimens.Default),
        verticalArrangement = Arrangement.spacedBy(AppDimens.Default),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppDimens.Default))
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(AppDimens.Default)
            ) {
                DetailItem(
                    icon = painterResource(R.drawable.ic_official),
                    title = stringResource(R.string.official_name),
                    content = holiday?.localName.orEmpty()
                )
                HorizontalDivider()
                DetailItem(
                    icon = painterResource(R.drawable.ic_origin),
                    title = stringResource(R.string.since),
                    content = (holiday?.launchYear ?: "-").toString()
                )
                HorizontalDivider()
                DetailItem(
                    icon = painterResource(R.drawable.ic_tag),
                    title = stringResource(R.string.category),
                    content = holiday?.types?.joinToString(", ") ?: ""
                )
            }
        }
        NotificationContent(checked, notificationEnabled) { onNotificationCheck(it) }
        AnimatedVisibility(error != null) {
            if (error != null) {
                val errorMessage = when (error) {
                    UiError.NotificationDisabledError -> stringResource(R.string.notification_disabled)
                    UiError.PastHolidayError -> stringResource(R.string.past_date)
                }
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
fun NotificationContent(
    checked: Boolean,
    notificationEnabled: Boolean,
    onNotificationCheck: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppDimens.Default),
        verticalArrangement = Arrangement.spacedBy(AppDimens.Small)
    ) {
        Text(
            text = stringResource(R.string.notification_description),
            color = MaterialTheme.colorScheme.onSurface
        )
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppDimens.Default))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppDimens.Default),
                horizontalArrangement = Arrangement.spacedBy(AppDimens.Default),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_notification),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.size(AppDimens.Large)
                )
                Text(
                    text = stringResource(R.string.notification),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = checked,
                    onCheckedChange = { onNotificationCheck(it) },
                    enabled = notificationEnabled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                )
            }
        }
    }
}

@Composable
fun DetailItem(icon: Painter, title: String, content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppDimens.Default),
        horizontalArrangement = Arrangement.spacedBy(AppDimens.Default),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier.size(AppDimens.Large)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AppDimens.Small)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = content,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SharedTransitionScope.DetailTop(
    date: LocalDate?,
    name: String,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Box(
        modifier = Modifier
            .size(AppDimens.HolidayIconDetailSize)
            .clip(RoundedCornerShape(AppDimens.Default))
            .background(Color.Black.copy(alpha = 0.15f))
            .padding(AppDimens.Default),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_calendar),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
    Text(
        text = name,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .sharedElement(
                sharedContentState = rememberSharedContentState(key = "date/$name"),
                animatedVisibilityScope = animatedVisibilityScope
            ),
        textAlign = TextAlign.Center
    )
    date?.let {
        Text(
            text = DateUtils.formatDateLocalized(date = it),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 20.sp
        )
    }
}

fun checkExactAlarmPermission(
    context: Context,
    onNotificationDenied: () -> Unit,
    onAllowed: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (alarmManager.canScheduleExactAlarms()) {
            onAllowed()
        } else {
            onNotificationDenied()
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(intent)
        }
    } else {
        onAllowed()
    }
}