package com.android.app.gametuner.ui.components.dialogs

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun ShizukuMissingDialog(onInstall: () -> Unit, onCancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = {}, // disables outside/back dismiss
        title = {
            Text(
                text = "Shizuku Not Found",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = "This app requires Shizuku to function. Please install Shizuku from the Play Store.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        confirmButton = {
            Button(
                onClick = onInstall,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Install",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}
