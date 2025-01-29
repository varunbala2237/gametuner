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
import androidx.compose.material.icons.filled.DisplaySettings
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.android.app.gametuner.global.GlobalDataManager
import com.android.app.gametuner.LocalMainActivity
import com.android.app.gametuner.StateStorage
import com.android.app.gametuner.ui.utils.getDeviceResolution

@androidx.compose.runtime.Composable
fun DisplaySettingsSection() {
    val context = LocalMainActivity.current
    val stateStorage = remember { StateStorage(context) }

    // Access the global apply settings switch state
    val applySettingsEnabled = GlobalDataManager.getApplySettingsState()

    // Detect the screen orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    val (deviceSize, deviceDpi) = getDeviceResolution(context)
    val deviceWidth = if (isLandscape) deviceSize.height else deviceSize.width
    val deviceHeight = if (isLandscape) deviceSize.width else deviceSize.height

    // Calculate downscaled resolutions with DPI
    val resolutionsWithDpi = getDownscaledResolutions(deviceWidth, deviceHeight, deviceDpi)

    // Retrieve the saved resolution list from StateStorage : if empty then initialize default values
    val savedResolutionList = stateStorage.getResolution()
    GlobalDataManager.setSelectedResolutionList(savedResolutionList)

    // initialize only resolution in dropdown content from stateStorage
    var selectedResolution by remember {
        mutableStateOf(
            if (savedResolutionList.firstOrNull() == "") "Default"
            else savedResolutionList.firstOrNull() ?: ""
        )
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
                imageVector = Icons.Filled.DisplaySettings,
                contentDescription = "Display Settings Icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Device Resolution",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Dropdown for resolution
        Box {
            OutlinedButton(
                onClick = { expanded = !expanded },
                enabled = !applySettingsEnabled
            ) {
                Text(
                    text = selectedResolution,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                resolutionsWithDpi.forEach { (resolution, dpi) ->
                    DropdownMenuItem(
                        onClick = {
                            selectedResolution = resolution
                            // Save the selected resolution and DPI to the global data manager
                            GlobalDataManager.setSelectedResolutionList(
                                if (resolution == "Default") listOf("", "reset", "", "reset") else listOf(resolution, "reset", dpi, "reset")
                            )
                            // Save the resolution to StateStorage
                            stateStorage.saveResolution(
                                if (resolution == "Default") listOf("", "reset", "", "reset") else listOf(resolution, "reset", dpi, "reset")
                            )
                            expanded = false
                        },
                        text = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = resolution,
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

// Downscaling resolution by factors
fun getDownscaledResolutions(width: Int, height: Int, originalDpi: Int): List<Pair<String, String>> {
    val resolutionsWithDpi = mutableListOf<Pair<String, String>>()

    var currentWidth: Int
    var currentHeight: Int
    var currentDpi: Int

    val factors = listOf(9.0/8, 5.0/4, 45.0/32, 3.0/2, 15.0/8, 2.0/1) // (9.0/4, 5.0/2)

    // Default resolution
    resolutionsWithDpi.add("Default" to "Default")

    for (factor in factors) {
        currentWidth = (width / factor).toInt()
        currentHeight = (height / factor).toInt()
        currentDpi = (originalDpi / factor).toInt()

        if (currentWidth < 540 || currentHeight < 1170) break

        resolutionsWithDpi.add("${currentWidth}x${currentHeight}" to "$currentDpi")
    }

    return resolutionsWithDpi
}