package dev.alejo.colombian_holidays.ui.home.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.alejo.colombian_holidays.R

@Composable
fun AppAlertDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(stringResource(R.string.widget_dialog_title))
        },
        text = {
            Text(stringResource(R.string.widget_dialog_description))
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() } ) {
                Text(stringResource(R.string.understood))
            }
        }
    )
}