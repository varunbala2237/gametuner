package com.android.app.gametuner.settings

import com.android.app.gametuner.shizuku.ShizukuHelper

// Thermal Profile Setting
fun applyDeviceProfile(isChecked: Boolean, deviceProfileList: List<String>) {
    val deviceProfile = deviceProfileList.getOrNull(0)

    val commands = listOf(
        "echo $deviceProfile > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor",
        "echo $deviceProfile > /sys/class/thermal/thermal_zone0/mode"
    )

    if (isChecked) {
        // Execute the commands through ShizukuHelper
        commands.forEach { command ->
            ShizukuHelper.executeShellCommandWithShizuku(
                command = command
            )
        }
    }
}