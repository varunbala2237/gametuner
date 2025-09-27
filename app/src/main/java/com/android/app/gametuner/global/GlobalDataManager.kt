package com.android.app.gametuner.global

import androidx.compose.runtime.mutableStateOf

object GlobalDataManager {
    // To track selected game as a live manager (MutableState to allow observation)
    private val selectedGame = mutableStateOf<String>("")

    // To track the apply settings switch state
    private val isApplySettingsEnabled = mutableStateOf(false)

    // To track the selected resolution
    private val selectedResolutionList = mutableStateOf<List<String>>(emptyList())

    // To track the memory cleaner switch state
    private val isMemoryCleanerEnabled = mutableStateOf(false)

    // To track the max fps switch state
    private val isMaxFps = mutableStateOf(false)

    // To track the force gpu rendering switch state
    private val isForceGpuRenderingEnabled = mutableStateOf(false)

    // Function to update the selected game
    fun setSelectedGame(gamePackageName: String) {
        selectedGame.value = gamePackageName
    }

    // Function to get the current selected game package name
    fun getSelectedGame(): String {
        return selectedGame.value
    }

    // Function to update the switch state
    fun setApplySettingsState(isEnabled: Boolean) {
        isApplySettingsEnabled.value = isEnabled
    }

    // Function to get the current state of the switch
    fun getApplySettingsState(): Boolean {
        return isApplySettingsEnabled.value
    }

    // Function to save the resolution list (including "reset")
    fun setSelectedResolutionList(resolutionList: List<String>) {
        selectedResolutionList.value = resolutionList
    }

    // Function to get the resolution list
    fun getSelectedResolutionList(): List<String> {
        return selectedResolutionList.value
    }

    // Function to update the switch state
    fun setMemoryCleanerState(isEnabled: Boolean) {
        isMemoryCleanerEnabled.value = isEnabled
    }

    // Function to get the current state of the switch
    fun getMemoryCleanerState(): Boolean {
        return isMemoryCleanerEnabled.value
    }

    // Function to update the switch state
    fun setForceGpuRendering(isEnabled: Boolean) {
        isForceGpuRenderingEnabled.value = isEnabled
    }

    // Function to get the current state of the switch
    fun getForceGpuRendering(): Boolean {
        return isForceGpuRenderingEnabled.value
    }
}