package com.android.app.gametuner.ui.components.fragments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.android.app.gametuner.ui.components.fragments.fragcomponents.DisplaySettingsSection
import com.android.app.gametuner.ui.components.fragments.fragcomponents.ForceRenderingSection
import com.android.app.gametuner.ui.components.fragments.fragcomponents.MemoryCleanerSection

@androidx.compose.runtime.Composable
fun DeviceFragment(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Display resolution settings
        DisplaySettingsSection()

        // RAM cleaner
        MemoryCleanerSection()

        // Force GPU Rendering
        ForceRenderingSection()
    }
}