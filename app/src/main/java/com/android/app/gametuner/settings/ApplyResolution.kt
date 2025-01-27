package com.android.app.gametuner.settings

import com.android.app.gametuner.shizuku.ShizukuHelper

// Resolution and DPI Setting
fun applyResolution(isChecked: Boolean, resolutionList: List<String>) {
    val resolution = resolutionList.getOrNull(0)
    val revertResolution = resolutionList.getOrNull(1)
    val dpi = resolutionList.getOrNull(2)
    val revertDpi = resolutionList.getOrNull(3)

    val commands = if (isChecked) {
        listOf(
            "wm size $resolution", // Apply resolution
            "wm density $dpi" // Apply dpi
        )
    } else {
        listOf(
            "wm size $revertResolution", // Revert resolution
            "wm density $revertDpi" // Revert DPI
        )
    }

    // Execute the commands through ShizukuHelper
    commands.forEach { command ->
        ShizukuHelper.executeShellCommandWithShizuku(
            command = command
        )
    }
}