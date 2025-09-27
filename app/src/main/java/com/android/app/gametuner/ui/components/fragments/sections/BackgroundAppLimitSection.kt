package com.android.app.gametuner.ui.components.fragments.sections

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
import androidx.compose.material.icons.filled.AppSettingsAlt // optional different icon
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.android.app.gametuner.LocalMainActivity
import com.android.app.gametuner.StateStorage
import com.android.app.gametuner.global.GlobalDataManager
import com.android.app.gametuner.global.GlobalLogsManager
import com.android.app.gametuner.settings.applyBackgroundAppLimit

@androidx.compose.runtime.Composable
fun BackgroundAppLimitSection() {
    val context = LocalMainActivity.current
    val stateStorage = remember { StateStorage(context) }

    // Apply settings switch state from global manager
    val applySettingsEnabled = GlobalDataManager.getApplySettingsState()

    // Get saved limit from storage and update global state
    val savedLimit = stateStorage.getBackgroundAppLimit()
    GlobalDataManager.setSelectedBackgroundAppLimit(savedLimit)
    GlobalLogsManager.addLog("Restored/Saved Background App Limit value: $savedLimit")

    // Ensure non-nullable mutable state
    var selectedLimit: String by remember { mutableStateOf(savedLimit) }
    var expanded by remember { mutableStateOf(false) }

    val limits = listOf("Default", "1x", "2x", "3x", "4x")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon on left
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AppSettingsAlt,
                contentDescription = "Background App Limit Icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        // Text label
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Background App Limit",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Dropdown menu
        Box {
            OutlinedButton(
                onClick = { expanded = !expanded },
                enabled = !applySettingsEnabled
            ) {
                Text(text = selectedLimit, style = MaterialTheme.typography.bodyMedium)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                limits.forEach { limit ->
                    DropdownMenuItem(
                        onClick = {
                            selectedLimit = limit
                            GlobalDataManager.setSelectedBackgroundAppLimit(limit)
                            stateStorage.saveBackgroundAppLimit(limit)
                            expanded = false

                            // Apply the background app limit if switch is enabled
                            if (applySettingsEnabled) {
                                applyBackgroundAppLimit(true, limit)
                            }
                        },
                        text = { Text(limit, style = MaterialTheme.typography.bodyMedium) }
                    )
                }
            }
        }
    }
}