package com.android.app.gametuner.global

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object GlobalDataManager {
    // To track the apply settings switch state
    private val isApplySettingsEnabled = mutableStateOf(false)

    // To track the selected resolution (as a list: [resolution, "reset"])
    private val selectedResolutionList = mutableStateOf<List<String>>(emptyList())

    // To track the memory cleaner switch state
    private val isMemoryCleanerEnabled = mutableStateOf(false)

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
}