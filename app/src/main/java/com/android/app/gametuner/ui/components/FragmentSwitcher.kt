package com.android.app.gametuner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.app.gametuner.ui.components.fragments.DeviceFragment
import com.android.app.gametuner.ui.components.fragments.GameFragment
import com.android.app.gametuner.ui.components.fragments.LogsFragment
import com.android.app.gametuner.ui.utils.darkenSurfaceColor
import com.android.app.gametuner.ui.utils.lightenSurfaceColor

@androidx.compose.runtime.Composable
fun FragmentSwitcher() {
    val isDarkMode = isSystemInDarkTheme()

    // To hold current fragment (either "Device" or "Game")
    var currentFragment by remember { mutableStateOf("Device") }

    // Switching fragments
    fun switchToFragment(fragment: String) {
        currentFragment = fragment
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(24.dp))
            .background(
                color = if (isDarkMode)
                    lightenSurfaceColor(MaterialTheme.colorScheme.surface)
                else
                    darkenSurfaceColor(MaterialTheme.colorScheme.surface)
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp)
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Device Tuning Button
                Button(
                    onClick = { switchToFragment("Device") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // Transparent background
                        contentColor = if (currentFragment == "Device") MaterialTheme.colorScheme.primary else Color.Gray
                    ),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icon for Device Tuning
                        Icon(
                            imageVector = Icons.Filled.Smartphone, // Material Icon for Device
                            contentDescription = "Device Icon",
                            tint = if (currentFragment == "Device") MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }

                // Game Tuning Button
                Button(
                    onClick = { switchToFragment("Game") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // Transparent background
                        contentColor = if (currentFragment == "Game") MaterialTheme.colorScheme.primary else Color.Gray
                    ),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icon for Game Tuning
                        Icon(
                            imageVector = Icons.Filled.SportsEsports, // Material Icon for Game
                            contentDescription = "Game Icon",
                            tint = if (currentFragment == "Game") MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }

                // Logs Button
                Button(
                    onClick = { switchToFragment("Logs") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // Transparent background
                        contentColor = if (currentFragment == "Logs") MaterialTheme.colorScheme.primary else Color.Gray
                    ),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icon for Logs
                        Icon(
                            imageVector = Icons.Filled.Info, // Material Icon for Logs
                            contentDescription = "Info Icon",
                            tint = if (currentFragment == "Logs") MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }
            }

            // Fragment content container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Device Tuning Fragment (Left)
                if (currentFragment == "Device") {
                    DeviceFragment(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                // Game Tuning Fragment (Center)
                if (currentFragment == "Game") {
                    GameFragment(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                // Logs Fragment (Right)
                if (currentFragment == "Logs") {
                    LogsFragment(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
@Preview
fun PreviewFragmentSwitcher() {
    FragmentSwitcher()
}