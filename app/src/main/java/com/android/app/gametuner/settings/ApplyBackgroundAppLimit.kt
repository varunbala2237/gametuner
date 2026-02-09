package com.android.app.gametuner.settings

import com.android.app.gametuner.global.GlobalLogsManager
import com.android.app.gametuner.shizuku.ShizukuHelper

fun applyBackgroundAppLimit(isChecked: Boolean, backgroundAppLimit: String) {
    if (!isChecked) return

    val command = when (backgroundAppLimit) {
        "1x" -> "settings put global background_process_limit 1"
        "2x" -> "settings put global background_process_limit 2"
        "3x" -> "settings put global background_process_limit 3"
        "4x" -> "settings put global background_process_limit 4"
        else -> "settings put global background_process_limit -1" // Default / unlimited
    }

    ShizukuHelper.executeShellCommandWithShizuku(command)
    GlobalLogsManager.addLog("Background running apps is limited to: $backgroundAppLimit")
}
