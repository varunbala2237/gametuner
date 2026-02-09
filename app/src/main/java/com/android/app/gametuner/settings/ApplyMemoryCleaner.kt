package com.android.app.gametuner.settings

import android.content.Context
import android.content.Intent
import com.android.app.gametuner.global.GlobalDataManager
import com.android.app.gametuner.global.GlobalLogsManager
import com.android.app.gametuner.services.MemoryCleanerService

fun applyMemoryCleaner(context: Context, isChecked: Boolean, isMemoryCleanerEnabled: Boolean) {
    // Retrieve the selected game package name from GlobalDataManager
    val selectedPackage = GlobalDataManager.getSelectedGame()

    // Start or stop memory cleaner service based on switch state
    val serviceIntent = Intent(context, MemoryCleanerService::class.java).apply {
        putExtra("selectedPackageName", selectedPackage)  // Pass the selected package name as an extra
    }

    if (isMemoryCleanerEnabled) {
        if (isChecked) {
            // Start the service
            context.startService(serviceIntent)
            GlobalLogsManager.addLog("Memory Cleaner Service starting with package: $selectedPackage")
        } else {
            // Stop the service
            context.stopService(serviceIntent)
        }
    }
}
