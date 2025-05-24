package dev.alejo.colombian_holidays.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape

object AppShapes {
    val LeftListNavigationButton = RoundedCornerShape(
        topStart = AppDimens.Small,
        topEnd = AppDimens.None,
        bottomStart = AppDimens.Small,
        bottomEnd = AppDimens.None
    )
    val rightListNavigationButton = RoundedCornerShape(
        topStart = AppDimens.None,
        topEnd = AppDimens.Small,
        bottomStart = AppDimens.None,
        bottomEnd = AppDimens.Small
    )
}