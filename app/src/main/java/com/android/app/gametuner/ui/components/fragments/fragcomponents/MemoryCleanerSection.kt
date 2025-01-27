package com.android.app.gametuner.ui.components.fragments.fragcomponents

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

    // Access the global apply settings switch state
    val applySettingsEnabled = GlobalDataManager.getApplySettingsState()

    // Retrieve the saved memory state
    val savedMemoryCleanerSwitchState = rememberSaveable { mutableStateOf(stateStorage.getMemoryCleanerSwitchState()) }

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
                text = "Enable this option to periodically free RAM resources",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        Switch(
            checked = savedMemoryCleanerSwitchState.value,
            onCheckedChange = { isChecked ->
                savedMemoryCleanerSwitchState.value = isChecked

                // Update the values in GlobalDataManager and stateStorage
                stateStorage.saveMemoryCleanerSwitchState(isChecked)
                GlobalDataManager.setMemoryCleanerState(isChecked)
                GlobalLogsManager.addLog("Restored/Saved Memory Cleaner Switch state: ${savedMemoryCleanerSwitchState.value}")
            }
        )
    }
}