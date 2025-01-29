package com.android.app.gametuner.settings

import com.android.app.gametuner.global.GlobalDataManager
import com.android.app.gametuner.global.GlobalLogsManager
import com.android.app.gametuner.shizuku.ShizukuHelper

// Thermal Profile Setting
fun applyDeviceProfile(isChecked: Boolean, deviceProfileList: List<String>) {
    // Get the selected profile
    val deviceProfile = deviceProfileList.getOrNull(0)

    // Check the number of CPU cores dynamically
    val cpuCoreCount = Runtime.getRuntime().availableProcessors()

    // Create a list of commands for each CPU core
    val commands = mutableListOf<String>()
    for (i in 0 until cpuCoreCount) {
        commands.add("echo $deviceProfile > /sys/devices/system/cpu/cpu$i/cpufreq/scaling_governor")
    }

    // Add the thermal zone command
    commands.add("echo $deviceProfile > /sys/class/thermal/thermal_zone0/mode")

    if (isChecked) {
        // Execute the commands through ShizukuHelper
        commands.forEach { command ->
            ShizukuHelper.executeShellCommandWithShizuku(
                command = command
            )
        }
    }
}