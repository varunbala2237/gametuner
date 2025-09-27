package com.android.app.gametuner.shizuku

import android.content.Context
import android.content.pm.PackageManager
import com.android.app.gametuner.global.GlobalLogsManager
import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuHelper {

    private fun checkPermission(): Boolean {
        if (Shizuku.isPreV11()) return false // Pre-v11 unsupported

        return try {
            when {
                Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED -> true
                Shizuku.shouldShowRequestPermissionRationale() -> false
                else -> false
            }
        } catch (e: Throwable) {
            false // binder not ready or any other error
        }
    }

    fun isShizukuInstalled(context: Context): Boolean {
        return try {
            context.packageManager.getPackageInfo("moe.shizuku.privileged.api", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun hasShizukuPermission(): Boolean {
        return checkPermission()
    }

    fun executeShellCommandWithShizuku(command: String) {
        if (!checkPermission()) {
            GlobalLogsManager.addLog("Shizuku permission not granted: Please grant permission and try again.")
            return
        }

        try {
            // Split the command into an array
            val cmd = command.split(" ".toRegex()).filter { it.isNotEmpty() }.toTypedArray()

            @Suppress("DEPRECATION")
            // Execute the command using ShizukuRemoteProcess
            val process = Shizuku.newProcess(cmd, null, null)

            // Read the output from the process
            val output = StringBuilder()
            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    output.append(line).append("\n")
                }
            }

            // Print the output to logs
            GlobalLogsManager.addLog("Command Output:\n$output")

            // Wait for the process to complete
            val exitCode = process.waitFor()
            GlobalLogsManager.addLog("Process exited with code: $exitCode")
        } catch (e: Exception) {
            GlobalLogsManager.addLog("Error executing command: ${e.message}")
        }
    }
}