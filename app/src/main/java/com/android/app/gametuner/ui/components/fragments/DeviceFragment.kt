package com.android.app.gametuner.ui.components.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.android.app.gametuner.ui.components.fragments.sections.DisplaySettingsSection
import com.android.app.gametuner.ui.components.fragments.sections.ForceRenderingSection
import com.android.app.gametuner.ui.components.fragments.sections.MemoryCleanerSection

@androidx.compose.runtime.Composable
fun DeviceFragment(modifier: Modifier = Modifier) {
    val listState = rememberLazyListState() // Scroll state
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val logsHeight = screenHeight * 0.38f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(logsHeight)
    ) {

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Device Settings",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                // Display resolution settings
                DisplaySettingsSection()
            }
            item {
                // RAM cleaner
                MemoryCleanerSection()
            }
            item {
                // Force GPU Rendering
                ForceRenderingSection()
            }
        }
    }
}