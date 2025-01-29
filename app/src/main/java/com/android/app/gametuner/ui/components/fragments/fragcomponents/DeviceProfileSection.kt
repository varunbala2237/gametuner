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
import androidx.compose.material.icons.filled.Speed
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

@androidx.compose.runtime.Composable
fun DeviceProfileSection() {
    val context = LocalMainActivity.current
    val stateStorage = remember { StateStorage(context) }

    // Access the global apply settings switch state
    val applySettingsEnabled = GlobalDataManager.getApplySettingsState()

    // Predefined device profiles (command, user-friendly name)
    val predefinedDeviceProfiles = listOf(
        Pair("interactive", "Default"),
        Pair("performance", "Performance"),
        Pair("balanced", "Balanced"),
        Pair("powersave", "Power Saving")
    )

    // Retrieve the saved device profile from StateStorage
    val savedDeviceProfileList = stateStorage.getDeviceProfile()
    GlobalDataManager.setSelectedDeviceProfileList(savedDeviceProfileList)

    // Initialize the selected device profile from the saved list
    var selectedDeviceProfile by remember {
        mutableStateOf(getProfileNameByCommand(savedDeviceProfileList.first()))
    }

    // State for managing the dropdown menu
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Speed,
                contentDescription = "Speed Icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Device Profile",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Dropdown for thermal profile
        Box {
            OutlinedButton(
                onClick = { expanded = !expanded },
                enabled = !applySettingsEnabled
            ) {
                Text(
                    text = selectedDeviceProfile,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                predefinedDeviceProfiles.forEach { (command, profileName) ->
                    DropdownMenuItem(
                        onClick = {
                            selectedDeviceProfile = profileName
                            // Save the command and profile name pair
                            GlobalDataManager.setSelectedDeviceProfileList(listOf(command))
                            stateStorage.saveDeviceProfile(listOf(command))
                            expanded = false
                        },
                        text = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = profileName,  // Displaying user-friendly name
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

// Helper function to get profile name from the command
fun getProfileNameByCommand(command: String): String {
    return when (command) {
        "interactive" -> "Default"
        "performance" -> "Performance"
        "balanced" -> "Balanced"
        "powersave" -> "Power Saving"
        else -> "auto"
    }
}