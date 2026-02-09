package com.android.app.gametuner

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StateStorage(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("GameTunerPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        // Unique keys
        private const val GAME_CONTENT_STATES_KEY = "game_content_states"
        private const val APPLY_SETTINGS_SWITCH_STATE_KEY = "apply_settings_switch_state"
        private const val SELECTED_RESOLUTION_KEY = "selected_resolution"
        private const val SELECTED_MEMORY_CLEANER_STATE_KEY = "selected_memory_cleaner"
        private const val SELECTED_BACKGROUND_APP_LIMIT_KEY = "selected_background_app_limit"
        private const val FORCE_GPU_RENDERING_SWITCH_STATE_KEY = "force_gpu_rendering_switch_state"
    }

    // Data class to hold both content and state, including the gameScrollState as Int
    data class GameContentStates(
        val games: List<String>, // List of game package names
        val selectedGame: String, // State for selected game
        val gameScrollState: Int // Game scroll state as Int
    )

    /* Save content and states together in a single structure */
    fun saveGameContentStates(gameContentStates: GameContentStates) {
        val json = gson.toJson(gameContentStates)  // Convert the object to JSON string
        sharedPreferences.edit().putString(GAME_CONTENT_STATES_KEY, json).apply()
    }

    /* Retrieve the saved content and states */
    fun getGameContentStates(): GameContentStates {
        var gameContentStates = sharedPreferences.getString(GAME_CONTENT_STATES_KEY, null)
        if (gameContentStates == null) {
            // Initialize with empty values if no saved state is found
            val initialState = GameContentStates(
                games = emptyList(),
                selectedGame = "",
                gameScrollState = 0
            )
            saveGameContentStates(initialState) // Save the initial state
            gameContentStates = gson.toJson(initialState) // Update the stored JSON
        }

        val type = object : TypeToken<GameContentStates>() {}.type
        return gson.fromJson(gameContentStates, type)
    }

    // Save switch state
    fun saveApplySettingsSwitchState(isChecked: Boolean) {
        sharedPreferences.edit().putBoolean(APPLY_SETTINGS_SWITCH_STATE_KEY, isChecked).apply()
    }

    // Get switch state
    fun getApplySettingsSwitchState(): Boolean {
        return sharedPreferences.getBoolean(APPLY_SETTINGS_SWITCH_STATE_KEY, false)
    }

    // Save resolution as a list
    fun saveResolution(resolutionList: List<String>) {
        val json = gson.toJson(resolutionList)  // Convert the list to JSON string
        sharedPreferences.edit().putString(SELECTED_RESOLUTION_KEY, json).apply()
    }

    // Get resolution as a list
    fun getResolution(): List<String> {
        val resolutionList = sharedPreferences.getString(SELECTED_RESOLUTION_KEY, null)

        if (resolutionList == null) {
            // Initialize with default values if no saved resolution is found
            val defaultResolutions = listOf("reset", "reset")
            saveResolution(defaultResolutions)
            return defaultResolutions
        }

        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(resolutionList, type)  // Deserialize the JSON string back into a List
    }

    // Save memory cleaner switch state
    fun saveMemoryCleanerSwitchState(isChecked: Boolean) {
        sharedPreferences.edit().putBoolean(SELECTED_MEMORY_CLEANER_STATE_KEY, isChecked).apply()
    }

    // Get memory cleaner switch state
    fun getMemoryCleanerSwitchState(): Boolean {
        return sharedPreferences.getBoolean(SELECTED_MEMORY_CLEANER_STATE_KEY, false)
    }

    // Save background app limit
    fun saveBackgroundAppLimit(limit: String) {
        sharedPreferences.edit().putString(SELECTED_BACKGROUND_APP_LIMIT_KEY, limit).apply()
    }

    // Get background app limit
    fun getBackgroundAppLimit(): String {
        val savedLimit = sharedPreferences.getString(SELECTED_BACKGROUND_APP_LIMIT_KEY, "Default")
        if (savedLimit == null) {
            saveBackgroundAppLimit("Default")
            return "Default"
        }
        return savedLimit
    }

    // Save switch state
    fun saveForceGpuRenderingSwitchState(isChecked: Boolean) {
        sharedPreferences.edit().putBoolean(FORCE_GPU_RENDERING_SWITCH_STATE_KEY, isChecked).apply()
    }

    // Get switch state
    fun getForceGpuRenderingSwitchState(): Boolean {
        return sharedPreferences.getBoolean(FORCE_GPU_RENDERING_SWITCH_STATE_KEY, false)
    }
}
