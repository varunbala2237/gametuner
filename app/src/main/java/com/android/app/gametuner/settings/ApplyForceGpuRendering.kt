package com.android.app.gametuner.settings

import com.android.app.gametuner.shizuku.ShizukuHelper

fun applyForceGpuRendering(
    isChecked: Boolean,
    isForceGpuRenderingEnabled: Boolean
) {
    // Command to enable/disable Force GPU Rendering
    val enableGpuRenderingCommand = "settings put global force_gpu_rendering 1"
    val disableGpuRenderingCommand = "settings put global force_gpu_rendering 0"

    if(isForceGpuRenderingEnabled) {
        if (isChecked) {
            // Enable Force GPU Rendering
            ShizukuHelper.executeShellCommandWithShizuku(enableGpuRenderingCommand)
        } else {
            // Disable Force GPU Rendering
            ShizukuHelper.executeShellCommandWithShizuku(disableGpuRenderingCommand)
        }
    }
}
