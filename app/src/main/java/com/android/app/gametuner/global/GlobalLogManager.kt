package com.android.app.gametuner.global

import androidx.compose.runtime.mutableStateListOf

object GlobalLogsManager {
    private val _logMessages = mutableListOf<String>()
    val logMessages: List<String>
        get() = _logMessages

    // Define the maximum log limit
    private const val MAX_LOG_LINES = 30

    // Use a mutable state to notify observers
    private val logMessagesState = mutableStateListOf<String>()

    fun addLog(log: String) {
        if (_logMessages.size >= MAX_LOG_LINES) {
            // Clear the previous logs if the limit is reached
            _logMessages.clear()
            logMessagesState.clear()  // Clear state as well
        }

        // Add the new log entry
        _logMessages.add(log)
        logMessagesState.add(log)  // Notify observers
    }

    fun clearLogs() {
        _logMessages.clear()
        logMessagesState.clear()  // This will notify observers
    }

    fun getLogs() = logMessagesState.toList()
}