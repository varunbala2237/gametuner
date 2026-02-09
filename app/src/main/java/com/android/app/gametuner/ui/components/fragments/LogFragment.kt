package com.android.app.gametuner.ui.components.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.android.app.gametuner.global.GlobalLogsManager
import com.android.app.gametuner.ui.theme.logTextStyle

@Composable
fun LogsFragment(modifier: Modifier = Modifier) {
    // Observe the logs state
    val log = GlobalLogsManager.getLog()
    val listState = rememberLazyListState() // Scroll state
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val logsHeight = screenHeight * 0.66f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(logsHeight)
    ) {
        // Header with title + Clear button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Log Data",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (log.isNotEmpty()) {
                items(log) { data ->
                    Text(
                        text = data,
                        style = logTextStyle
                    )
                }
            } else {
                item {
                    Text(
                        text = "No data available",
                        style = logTextStyle
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Clear button aligned bottom-right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { GlobalLogsManager.clearLog() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = "Clear", style = MaterialTheme.typography.bodyMedium)
            }
        }

        // Auto-scroll effect
        LaunchedEffect(log.size) {
            if (log.isNotEmpty()) {
                listState.animateScrollToItem(log.size - 1)
            }
        }
    }
}
