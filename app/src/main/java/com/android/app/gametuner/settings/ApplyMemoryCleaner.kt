package com.android.app.gametuner.settings

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.android.app.gametuner.global.GlobalDataManager
import com.android.app.gametuner.global.GlobalLogsManager
import com.android.app.gametuner.services.MemoryCleanerService
import android.provider.Settings

fun applyMemoryCleaner(context: Context, isChecked: Boolean, isMemoryCleanerEnabled: Boolean) {
    // Retrieve the selected game package name from GlobalDataManager
    val selectedPackage = GlobalDataManager.getSelectedGame()


    // Check if the permission is granted
    if (isUsageAccessGranted(context)) {
        val serviceIntent = Intent(context, MemoryCleanerService::class.java).apply {
            putExtra("selectedPackageName", selectedPackage)  // Pass the selected package name as an extra
        }

        if (isMemoryCleanerEnabled) {
            if (isChecked) {
                // Start the service
                context.startService(serviceIntent)
                GlobalLogsManager.addLog("RAM Cleaner Service starting with package: $selectedPackage")
            } else {
                // Stop the service
                context.stopService(serviceIntent)
            }
        }
    } else {
        // If permission is not granted, guide the user to enable it in settings
        guideUserToSettings(context)
    }
}

fun isUsageAccessGranted(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
    return false
}

fun guideUserToSettings(context: Context) {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    context.startActivity(intent)
}