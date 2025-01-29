package com.android.app.gametuner.ui.components.fragments.fragcomponents

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.android.app.gametuner.LocalMainActivity
import com.android.app.gametuner.StateStorage
import com.android.app.gametuner.global.GlobalDataManager
import com.android.app.gametuner.global.GlobalLogsManager

@androidx.compose.runtime.Composable
fun MemoryCleanerSection() {
    val context = LocalMainActivity.current
    val stateStorage = remember { StateStorage(context) }

    // Check if the device is running Android 10 or above
    val isAndroid10OrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    // Access the global apply settings switch state
    val applySettingsEnabled = GlobalDataManager.getApplySettingsState()

    // Retrieve the saved memory state
    val memoryCleanerSwitchState = rememberSaveable { mutableStateOf(stateStorage.getMemoryCleanerSwitchState()) }

    LaunchedEffect(memoryCleanerSwitchState.value) {
        // Update the values in GlobalDataManager and stateStorage
        stateStorage.saveMemoryCleanerSwitchState(memoryCleanerSwitchState.value)
        GlobalDataManager.setMemoryCleanerState(memoryCleanerSwitchState.value)
        GlobalLogsManager.addLog("Restored/Saved Memory Cleaner Switch state: ${memoryCleanerSwitchState.value}")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CleaningServices,
                contentDescription = "Cleaning Services Icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "RAM Cleaner",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Enable this option to free RAM resources in background",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        Switch(
            checked = memoryCleanerSwitchState.value,
            onCheckedChange = { isChecked ->
                if (isAndroid10OrAbove) {
                    // Check if usage access permission is granted
                    if (isUsageAccessGranted(context)) {
                        memoryCleanerSwitchState.value = isChecked
                    } else {
                        // Inform the user to grant permission
                        Toast.makeText(context, "Please enable Usage Access to use RAM Cleaner", Toast.LENGTH_LONG).show()
                        guideUserToSettings(context) // Guide the user to the settings page
                    }
                } else {
                    // Disable the switch if Android version is below 10
                    Toast.makeText(context, "RAM Cleaner requires Android 10 or above", Toast.LENGTH_LONG).show()
                }
            },
            enabled = !applySettingsEnabled
        )
    }
}

fun isUsageAccessGranted(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
    return false
}

fun guideUserToSettings(context: Context) {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    context.startActivity(intent)
}