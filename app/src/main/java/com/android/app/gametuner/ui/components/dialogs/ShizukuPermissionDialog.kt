package com.android.app.gametuner.ui.components.dialogs

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import rikka.shizuku.Shizuku

@Composable
fun ShizukuPermissionDialog(onOpen: () -> Unit, onGrant: () -> Unit) {
    val requestPermissionCode = 1001
    var permissionGranted by remember { mutableStateOf(false) }

    // Listener for permission result
    val listener = remember {
        Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
            if (requestCode == requestPermissionCode && grantResult == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true
            }
        }
    }

    // Register listener on composition, remove on dispose
    DisposableEffect(Unit) {
        Shizuku.addRequestPermissionResultListener(listener)
        onDispose {
            Shizuku.removeRequestPermissionResultListener(listener)
        }
    }

    // Check initial permission state
    LaunchedEffect(Unit) {
        try {
            if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true
            }
        } catch (_: Throwable) {
            // Binder not ready
        }
    }

    if (!permissionGranted) {
        AlertDialog(
            onDismissRequest = {}, // non-cancelable
            title = {
                Text(
                    text = "Shizuku Permission Required",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "GameTuner requires Shizuku to be paired and granted permission to function. " +
                            "Please setup Shizuku first and grant the permission.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onOpen,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Open Shizuku", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onGrant,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Grant Permission", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}
