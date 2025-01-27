package com.android.app.gametuner.shizuku

import java.io.BufferedReader
import java.io.InputStreamReader
import rikka.shizuku.Shizuku

object ShizukuHelperForServices {

    // Lightweight command execution without logs or permission checks
    fun executeShellCommands(command: String) {
        try {
            val cmd = command.split(" ".toRegex()).filter { it.isNotEmpty() }.toTypedArray()

            @Suppress("DEPRECATION")
            val process = Shizuku.newProcess(cmd, null, null)

            val output = StringBuilder()
            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    output.append(line).append("\n")
                }
            }

            process.waitFor()
        } catch (e: Exception) {
            // Do nothing, no logging
        }
    }
}