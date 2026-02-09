package com.android.app.gametuner.ui

import android.content.pm.PackageInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.app.gametuner.global.GlobalDataManager
import com.android.app.gametuner.global.GlobalLogsManager
import com.android.app.gametuner.LocalMainActivity
import com.android.app.gametuner.StateStorage
import com.android.app.gametuner.ui.components.AppBar
import com.android.app.gametuner.ui.components.ApplySettingsSection
import com.android.app.gametuner.ui.components.FragmentSwitcher
import com.android.app.gametuner.ui.components.GameIconsRow
import com.android.app.gametuner.ui.components.PlayButton

@Composable
fun GameTunerApp() {
    val context = LocalMainActivity.current
    val stateStorage = remember { StateStorage(context) }

    // Retrieve saved game content and states from SharedPreferences
    val gameContentStates = remember { mutableStateOf(stateStorage.getGameContentStates()) }

    // Retrieve installed games list, selectedGamePackage, gameScrollState
    val installedGames = remember { mutableStateOf<List<PackageInfo>>(emptyList()) }
    val selectedGamePackage = rememberSaveable {
        mutableStateOf(gameContentStates.value.selectedGame)
    }
    val gameScrollState = remember { mutableStateOf(gameContentStates.value.gameScrollState) }

    // Apply settings switch
    val switchState = rememberSaveable { mutableStateOf(stateStorage.getApplySettingsSwitchState()) }

    // Load installed games when the composable first loads
    installedGames.value = context.getInstalledGames()

    LaunchedEffect(installedGames.value) {
        // If no game is selected (selectedGamePackage is null), select the first game
        if (selectedGamePackage.value.isEmpty() && installedGames.value.isNotEmpty()) {
            selectedGamePackage.value = installedGames.value.first().packageName

            // Update stateStorage with the first selected game package
            stateStorage.saveGameContentStates(
                StateStorage.GameContentStates(
                    games = installedGames.value.map { it.packageName },
                    selectedGame = selectedGamePackage.value,
                    gameScrollState = 0 // Set initial scroll state to 0
                )
            )
        }
    }

    // Combine saving selectedGamePackage and gameScrollState into a single LaunchedEffect
    LaunchedEffect(selectedGamePackage.value, gameScrollState.value) {
        stateStorage.saveGameContentStates(
            StateStorage.GameContentStates(
                games = installedGames.value.map { it.packageName },
                selectedGame = selectedGamePackage.value,
                gameScrollState = gameScrollState.value // Save both selected game and scroll state
            )
        )

        // Update the selected game in GlobalDataManager
        GlobalDataManager.setSelectedGame(selectedGamePackage.value)

        // Log the selected game and scroll state for debugging
        GlobalLogsManager.addLog("Saved/Restored selected game: ${selectedGamePackage.value}")
        GlobalLogsManager.addLog("Saved/Restored scroll state: ${gameScrollState.value}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // App bar
        AppBar()
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(18.dp))
        ) {
            item {
                // Game icons
                GameIconsRow(installedGames.value, selectedGamePackage, gameScrollState)
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {// Apply switch
                ApplySettingsSection(switchState)
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Game tuning and Device Tuning
                FragmentSwitcher()
            }
        }
    }

    // FAB for launching games
    PlayButton(selectedGamePackage)
}

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    GameTunerApp()
}
