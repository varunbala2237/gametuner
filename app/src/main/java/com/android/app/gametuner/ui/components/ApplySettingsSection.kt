package com.android.app.gametuner.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.android.app.gametuner.LocalMainActivity
import com.android.app.gametuner.StateStorage
import com.android.app.gametuner.global.GlobalDataManager
import com.android.app.gametuner.global.GlobalLogsManager
import com.android.app.gametuner.settings.applyForceGpuRendering
import com.android.app.gametuner.settings.applyMemoryCleaner
import com.android.app.gametuner.settings.applyResolution
import com.android.app.gametuner.shizuku.ShizukuHelper
import com.android.app.gametuner.ui.utils.darkenSurfaceColor
import com.android.app.gametuner.ui.utils.lightenSurfaceColor

@androidx.compose.runtime.Composable
fun ApplySettingsSection(
    switchState: MutableState<Boolean>
) {
    val context = LocalMainActivity.current
    val stateStorage = remember { StateStorage(context) }

    // Determine if in light or dark mode
    val isDarkMode = isSystemInDarkTheme()

    // Retrieve the resolution list from GlobalDataManager
    val resolutionList = GlobalDataManager.getSelectedResolutionList()

    // Retrieve the memory cleaner state from GlobalDataManager
    val isMemoryCleanerEnabled = GlobalDataManager.getMemoryCleanerState()

    // Retrieve the force gpu rendering switch state from GlobalDataManager
    val isForceGpuRenderingEnabled = GlobalDataManager.getMemoryCleanerState()

    // Live check for Shizuku permission and installation
    LaunchedEffect(Unit) {
        if (!ShizukuHelper.isShizukuInstalled(context) || !ShizukuHelper.hasShizukuPermission()) {
            // Update switch state to false
            switchState.value = false
        }
    }

    LaunchedEffect(switchState.value) {
        // Update the values in GlobalDataManager and StateStorage
        stateStorage.saveApplySettingsSwitchState(switchState.value)
        GlobalDataManager.setApplySettingsState(switchState.value)
        GlobalLogsManager.addLog("Restored/Saved Apply Settings Switch state: ${switchState.value}")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                color = if (isDarkMode)
                    lightenSurfaceColor(MaterialTheme.colorScheme.surface)
                else
                    darkenSurfaceColor(MaterialTheme.colorScheme.surface)
            )
            .padding(18.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings Icon",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Apply Settings",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Switch(
                checked = switchState.value,
                onCheckedChange = { isChecked ->
                    when {
                        !ShizukuHelper.isShizukuInstalled(context) -> {
                            Toast.makeText(context, "Shizuku not found", Toast.LENGTH_SHORT).show()
                        }

                        !ShizukuHelper.hasShizukuPermission() -> {
                            Toast.makeText(context, "Shizuku permission not granted", Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            switchState.value = isChecked

                            // Apply custom resolution
                            applyResolution(
                                isChecked = isChecked,
                                resolutionList = resolutionList
                            )

                            // Apply memory cleaner service
                            applyMemoryCleaner(
                                context = context,
                                isChecked = isChecked,
                                isMemoryCleanerEnabled = isMemoryCleanerEnabled
                            )

                            applyForceGpuRendering(
                                isChecked = isChecked,
                                isForceGpuRenderingEnabled = isForceGpuRenderingEnabled
                            )
                        }
                    }
                }
            )
        }
    }
}