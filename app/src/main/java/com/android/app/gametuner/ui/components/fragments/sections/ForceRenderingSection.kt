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
import androidx.compose.material.icons.filled.ElectricBolt
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
fun ForceRenderingSection() {
    val context = LocalMainActivity.current
    val stateStorage = remember { StateStorage(context) }

    // Access the global apply settings switch state
    val applySettingsEnabled = GlobalDataManager.getApplySettingsState()

    // Retrieve the saved force gpu rendering switch state
    val forceGpuRenderingSwitchState = rememberSaveable { mutableStateOf(stateStorage.getForceGpuRenderingSwitchState()) }

    LaunchedEffect(forceGpuRenderingSwitchState.value) {
        // Update the values in GlobalDataManager and stateStorage
        stateStorage.saveForceGpuRenderingSwitchState(forceGpuRenderingSwitchState.value)
        GlobalDataManager.setForceGpuRendering(forceGpuRenderingSwitchState.value)
        GlobalLogsManager.addLog("Restored/Saved Force GPU Rendering Switch state: ${forceGpuRenderingSwitchState.value}")
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
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.ElectricBolt,
                contentDescription = "Electric Bolt Icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Force GPU Rendering",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Switch(
            checked = forceGpuRenderingSwitchState.value,
            onCheckedChange = { isChecked ->
                // Update the switch state
                forceGpuRenderingSwitchState.value = isChecked
            },
            enabled = !applySettingsEnabled
        )
    }
}