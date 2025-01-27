package com.android.app.gametuner.ui.components.fragments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.app.gametuner.ui.components.fragments.fragcomponents.DisplaySettingsSection
import com.android.app.gametuner.ui.components.fragments.fragcomponents.ForceRenderingSection
import com.android.app.gametuner.ui.components.fragments.fragcomponents.MemoryCleanerSection
import com.android.app.gametuner.ui.components.fragments.fragcomponents.ThermalProfileSection
import com.android.app.gametuner.ui.components.fragments.fragcomponents.VulkanSettingSection

@androidx.compose.runtime.Composable
fun DeviceFragment(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Display resolution settings
        DisplaySettingsSection()
        Spacer(modifier = Modifier.height(16.dp))

        // RAM cleaner
        MemoryCleanerSection()
        Spacer(modifier = Modifier.height(16.dp))

        // Thermal Profile
        ThermalProfileSection()
        Spacer(modifier = Modifier.height(16.dp))

        // Force GPU Rendering
        ForceRenderingSection()
        Spacer(modifier = Modifier.height(16.dp))

        // Vulkan API
        VulkanSettingSection()
    }
}