package com.android.app.gametuner.settings

import com.android.app.gametuner.shizuku.ShizukuHelper

fun applyMaxFps(
    isChecked: Boolean,
    isMaxFps: Boolean
) {
    // Command to enable/disable Max FPS

    if(isMaxFps) {
        if (isChecked) {
            // Enable Max FPS
            ShizukuHelper.executeShellCommandWithShizuku("settings put system peak_refresh_rate 60")
            ShizukuHelper.executeShellCommandWithShizuku("settings put system min_refresh_rate 60")
        } else {
            // Disable MAx FPS
            // ShizukuHelper.executeShellCommandWithShizuku()
        }
    }
}