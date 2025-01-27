package com.android.app.gametuner.ui.components.fragments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.app.gametuner.global.GlobalLogsManager
import com.android.app.gametuner.ui.theme.logTextStyle

@Composable
fun LogsFragment(modifier: Modifier = Modifier) {
    // Observe the logs state
    val logs = GlobalLogsManager.getLogs()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Using LazyColumn to efficiently display logs
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (logs.isNotEmpty()) {
                logs.forEach { log ->
                    Text(
                        text = log,
                        style = logTextStyle,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            } else {
                Text(
                    text = "No logs available",
                    style = logTextStyle
                )
            }
        }
    }
}