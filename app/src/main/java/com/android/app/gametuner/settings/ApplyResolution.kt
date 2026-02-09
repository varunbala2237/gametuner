package com.android.app.gametuner.settings

import com.android.app.gametuner.global.GlobalLogsManager
import com.android.app.gametuner.shizuku.ShizukuHelper

// Resolution and DPI Setting
fun applyResolution(isChecked: Boolean, resolutionList: List<String>) {
    val resolution = resolutionList.getOrNull(0)
    val dpi = resolutionList.getOrNull(1)

    val commands = listOf(
        "wm size $resolution", // Apply resolution
        "wm density $dpi" // Apply dpi
    )

    if (isChecked) {
        // Execute the commands through ShizukuHelper
        commands.forEach { command ->
            ShizukuHelper.executeShellCommandWithShizuku(
                command = command
            )
        }
        GlobalLogsManager.addLog("Display resolution applied is: $resolutionList")
    }
}
